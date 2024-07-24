package com.example.myplaylist.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.player.domain.PlayerInteractor
import com.example.myplaylist.player.domain.PlayerInteractorImpl
import com.example.myplaylist.player.domain.PlayerStateChangeListener
import com.example.myplaylist.player.domain.db.HistoryRepositoryDatabase
import com.example.myplaylist.player.domain.use_case.MediaPlayerUseCase
import com.example.myplaylist.player.domain.use_case.TimerUseCase
import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.END_TIME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch

class MediaPlayViewModel(
    mediaPlayerUseCase: MediaPlayerUseCase,
    timerUseCase: TimerUseCase,
    private var historyRepositoryDatabase: HistoryRepositoryDatabase
) : ViewModel(), PlayerStateChangeListener {

    private val playerInteractor: PlayerInteractor = PlayerInteractorImpl(
        mediaPlayerUseCase,
        timerUseCase,
        viewModelScope
    )

    private val _track = MutableStateFlow<Track?>(null)
    val track: StateFlow<Track?> = _track.asStateFlow()

    private val _currentTime = MutableStateFlow(END_TIME)
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.PAUSE)
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _isTrackFavorite = MutableLiveData<Boolean>()
    val isTrackFavorite: LiveData<Boolean> get() = _isTrackFavorite

    init {
        viewModelScope.launch {
            playerInteractor.playerStateFlow.collect { state ->
                _playerState.value = state
            }
        }
        viewModelScope.launch {
            playerInteractor.track.collect { track ->
                _track.value = track
            }
        }
        viewModelScope.launch {
            playerInteractor.currentTimeFlow.collect { time ->
                Log.d("MyLog", "_currentTime.value = time  $time")
                _currentTime.value = time
            }
        }
    }

    fun setTrack(track: Track) {
        viewModelScope.launch {
            playerInteractor.setTrack(track)
            _playerState.value = PlayerState.PAUSE
        }
    }

    fun playOrPause() {
        viewModelScope.launch {
            playerInteractor.playOrPause()
        }
    }

    override fun onTimeUpdate(currentPosition: Int) {
        playerInteractor.updateTime(currentPosition)
    }

    private fun stopPlayer() {
        viewModelScope.launch {
            playerInteractor.stopPlayer()
        }
    }

    private fun resetTimer() {
        viewModelScope.launch {
            playerInteractor.setResetTimer()
        }
    }

    fun stop() {
        viewModelScope.launch {
            resetTimer()
            playerInteractor.stopPlayback()
            _playerState.value = PlayerState.PAUSE
            stopPlayer()
            Log.d("MyLog", "stop called, state reset to PAUSE")
        }
    }
    fun saveTrackOnFavorite(track: Track) {
        viewModelScope.launch {
            historyRepositoryDatabase.historyTrackDatabase().collect { tracks ->
                val updatedTracks = tracks.toMutableList()
                updatedTracks.add(track)
                historyRepositoryDatabase.saveTracks(updatedTracks)
                checkTrackIsFavorite(track)
            }
        }
    }
    fun deleteTrackOnFavorite(track: Track) {
        viewModelScope.launch {
            historyRepositoryDatabase.historyTrackDatabase().collect {
                historyRepositoryDatabase.deleteTrackId(track)
                checkTrackIsFavorite(track)
            }
        }
    }
    fun checkTrackIsFavorite(track: Track) {
        viewModelScope.launch {
            _isTrackFavorite.value = historyRepositoryDatabase.checkTrackIsFavorite(track)
        }
    }
    fun deleteAll() {
        viewModelScope.launch {
            historyRepositoryDatabase.clearHistory()
        }

    }
}