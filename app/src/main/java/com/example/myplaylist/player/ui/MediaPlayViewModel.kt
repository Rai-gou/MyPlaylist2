package com.example.myplaylist.player.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.player.domain.PlayerInteractor
import com.example.myplaylist.player.domain.PlayerStateChangeListener
import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaPlayViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel(), PlayerStateChangeListener {

    private val _track = MutableStateFlow<Track?>(null)
    val track: StateFlow<Track?> = _track.asStateFlow()

    private val _currentTime = MutableStateFlow("00:00")
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.PAUSE)
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        coroutinePlay()
        coroutineTrack()
        coroutineTime()
    }
    private fun coroutinePlay() {
        coroutineScope.launch {
            playerInteractor.playerStateFlow.collect { state ->
                _playerState.value = state
            }
        }
    }
    private fun coroutineTrack() {
        coroutineScope.launch {
            playerInteractor.track.collect { track ->
                _track.value = track
            }
        }
    }
    private fun coroutineTime() {
        coroutineScope.launch {
            playerInteractor.currentTimeFlow.collect { time ->
                Log.d("MyLog", "_currentTime.value = time  $time")
                _currentTime.value = time
            }
        }
    }


    fun setTrack(track: Track) {
        playerInteractor.setTrack(track)
        _playerState.value = PlayerState.PAUSE
    }

    fun playOrPause() {
        playerInteractor.playOrPause()
    }

    override fun onTimeUpdate(currentPosition: Int) {
        playerInteractor.updateTime(currentPosition)
    }

    private fun stopPlayer() {
        playerInteractor.stopPlayer()
    }
    private fun resetTimer() {
        playerInteractor.setResetTimer()
    }

    fun stop() {
        resetTimer()
        playerInteractor.stopPlayback()
        _playerState.value = PlayerState.PAUSE
        stopPlayer()
        coroutineScope.cancel()
        Log.d("MyLog", "stop called, state reset to PAUSE")
    }
}