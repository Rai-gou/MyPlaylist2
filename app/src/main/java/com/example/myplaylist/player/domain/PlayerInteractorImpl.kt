package com.example.myplaylist.player.domain

import android.util.Log
import com.example.myplaylist.player.domain.use_case.MediaPlayerUseCase
import com.example.myplaylist.player.domain.use_case.TimerUseCase
import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.DateTimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerInteractorImpl(
    private val mediaPlayerUseCase: MediaPlayerUseCase,
    private val timerUseCase: TimerUseCase,
    private val scope: CoroutineScope
) : PlayerInteractor, TimerUpdate, PlayerStateChangeListener {

    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.PAUSE)
    override val playerStateFlow: Flow<PlayerState> get() = _playerStateFlow.asStateFlow()

    private val _track = MutableStateFlow<Track?>(null)
    override val track: StateFlow<Track> get() = _track.filterNotNull().stateIn(
        scope,
        SharingStarted.Eagerly,
        Track(
            trackId ="",
            trackName = "",
            artistName = "",
            trackTimeMillis = null,
            artworkUrl100 = "",
            previewUrl = "",
            collectionName = "",
            releaseDate = "",
            primaryGenreName = "",
            country = "",
            addedTimestamp = null,
            addedTimePlaylist = null
        )
    )

    private val _currentTimeFlow = MutableStateFlow("00:00")
    override val currentTimeFlow: Flow<String> get() = _currentTimeFlow.asStateFlow()

    init {
        timerUseCase.setTimerUpdateListener(this)
        scope.launch {
            _track.collect {
                Log.d("TrackFlow", "TrackEntity updated: ${_track.value}")
            }
        }
    }

    override suspend fun setTrack(track: Track) {
        scope.launch {
            _track.value = track
            mediaPlayerUseCase.setDataSource(track.previewUrl)
            mediaPlayerUseCase.setOnCompletionListener {
                resetTimer()
            }
            _playerStateFlow.value = PlayerState.PAUSE
            mediaPlayerUseCase.prepareAsync { mediaPlayerUseCase.seekToStart() }
        }
    }

    override suspend fun playOrPause() {
        scope.launch {
            if (_playerStateFlow.value == PlayerState.PLAY) {
                mediaPlayerUseCase.pause()
                _playerStateFlow.value = PlayerState.PAUSE
                stopUpdatingTime()
                mediaPlayerUseCase.seekTo(mediaPlayerUseCase.currentPosition())
            } else {
                mediaPlayerUseCase.resume()
                _playerStateFlow.value = PlayerState.PLAY
                startUpdatingTime()
            }
        }
    }

    override fun startUpdatingTime() {
        timerUseCase.startTimer(scope)
    }

    override fun stopUpdatingTime() {
        timerUseCase.stopTimer()
    }

    override suspend fun seekTo(position: Int) {
        scope.launch {
            mediaPlayerUseCase.seekTo(position)
        }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerUseCase.currentPosition()
    }

    override fun resetTimer() {
        scope.launch {
            timerUseCase.resetTimer()
            _playerStateFlow.value = PlayerState.PAUSE
            _currentTimeFlow.value = DateTimeUtil.simpleDateFormat(0)
        }
    }

    override suspend fun setResetTimer() {
        resetTimer()
    }

    override fun updateTime(currentPosition: Int) {
        val formattedTime = DateTimeUtil.simpleDateFormat(currentPosition.toLong())
        _currentTimeFlow.value = formattedTime
    }

    override fun onTimeUpdate(currentPosition: Int) {
        updateTime(currentPosition)
    }

    override suspend fun stopPlayback() {
        scope.launch {
            mediaPlayerUseCase.pause()
        }
    }

    override suspend fun stopPlayer() {
        scope.launch {
            mediaPlayerUseCase.stop()
        }
    }
}