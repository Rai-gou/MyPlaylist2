package com.example.myplaylist.player.domain

import android.util.Log
import com.example.myplaylist.player.domain.use_case.MediaPlayerUseCase
import com.example.myplaylist.player.domain.use_case.TimerUseCase
import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.DateTimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class PlayerInteractorImpl(
    private val mediaPlayerUseCase: MediaPlayerUseCase,
    private val timerUseCase: TimerUseCase
) : PlayerInteractor, TimerUpdate, PlayerStateChangeListener {

    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.PAUSE)
    override val playerStateFlow: Flow<PlayerState> get() = _playerStateFlow.asStateFlow()

    private val _track = MutableStateFlow<Track?>(null)
    override val track: StateFlow<Track> = _track.filterNotNull().stateIn(
        CoroutineScope(Dispatchers.Default),
        SharingStarted.Eagerly,
        Track(
            trackName = "",
            artistName = "",
            trackId = "",
            trackTimeMillis = null,
            artworkUrl100 = "",
            previewUrl = "",
            collectionName = "",
            releaseDate = "",
            primaryGenreName = "",
            country = ""
        )
    )

    private val _currentTimeFlow = MutableStateFlow("00:00")
    override val currentTimeFlow: Flow<String> get() = _currentTimeFlow.asStateFlow()

    init {
        timerUseCase.setTimerUpdateListener(this)
    }

    override fun setTrack(track: Track) {
        _track.value = track
        mediaPlayerUseCase.setDataSource(track.previewUrl)
        mediaPlayerUseCase.setOnCompletionListener {
            resetTimer()
        }
        _playerStateFlow.value = PlayerState.PAUSE
        mediaPlayerUseCase.prepareAsync { mediaPlayerUseCase.seekToStart() }
    }

    override fun playOrPause() {
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

    override fun startUpdatingTime() {
        Log.d("MyLog", "startUpdatingTime")
        timerUseCase.startUpdatingTime()
    }

    override fun stopUpdatingTime() {
        Log.d("MyLog", "stopUpdatingTime")
        timerUseCase.stopUpdatingTime()
    }

    override fun seekTo(position: Int) {
        mediaPlayerUseCase.seekTo(position)
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerUseCase.currentPosition()
    }

    override fun resetTimer() {
        timerUseCase.resetTimer()
        _playerStateFlow.value = PlayerState.PAUSE
        _currentTimeFlow.value = DateTimeUtil.simpleDateFormat(0)
    }

    override fun setResetTimer() {
        return resetTimer()
    }

    override fun updateTime(currentPosition: Int) {
        val formattedTime = DateTimeUtil.simpleDateFormat(currentPosition.toLong())
        Log.d("MyLog", "updateTime called with: $formattedTime")
        _currentTimeFlow.value = formattedTime
    }

    override fun onTimeUpdate(currentPosition: Int) {
        Log.d("MyLog", "onTimeUpdate: $currentPosition")
        updateTime(currentPosition)
    }
    override fun stopPlayback() {
        mediaPlayerUseCase.pause()
    }
    override fun stopPlayer() {
        mediaPlayerUseCase.stop()
    }
}