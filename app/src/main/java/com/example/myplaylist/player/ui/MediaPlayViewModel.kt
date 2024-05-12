package com.example.myplaylist.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myplaylist.player.domain.PlayerInteractor
import com.example.myplaylist.player.domain.PlayerStateChangeListener
import com.example.myplaylist.player.domain.TimerUpdate
import com.example.myplaylist.player.domain.use_case.MediaPlayerUseCase
import com.example.myplaylist.player.domain.use_case.TimerUseCase
import com.example.myplaylist.player.domain.use_case.TimerUseCaseImpl
import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.DateTimeUtil

class MediaPlayViewModel(
    private val mediaPlayerUseCase: MediaPlayerUseCase,
    private val timerUseCase: TimerUseCase,
    private val playerInteractor: PlayerInteractor,
) : ViewModel(), PlayerStateChangeListener, TimerUpdate {

    init {
        (timerUseCase as? TimerUseCaseImpl)?.setTimerUpdateListener(this)
    }

    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track> = _track

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> = _currentTime

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    init {
        playerInteractor.addStateChangeListener(this)
    }

    fun setTrack(track: Track) {
        _track.value = track
        mediaPlayerUseCase.setDataSource(track.previewUrl)
        mediaPlayerUseCase.setOnCompletionListener {
            _playerState.value = PlayerState.PAUSE
            resetTimer()
        }
        Log.d("MyLog", "setTrack")
        mediaPlayerUseCase.prepareAsync { mediaPlayerUseCase.seekToStart() }
    }

    fun playOrPause() {
        if (_playerState.value == PlayerState.PLAY) {
            mediaPlayerUseCase.pause()
            _playerState.value = PlayerState.PAUSE
            stopUpdatingTime()
            mediaPlayerUseCase.seekTo(mediaPlayerUseCase.currentPosition())
        } else {
            mediaPlayerUseCase.resume()
            _playerState.value = PlayerState.PLAY
            startUpdatingTime()

        }
    }

    override fun startUpdatingTime() {
        timerUseCase.startUpdatingTime()
    }

    override fun stopUpdatingTime() {
        timerUseCase.stopUpdatingTime()
    }

    override fun resetTimer() {
        timerUseCase.resetTimer()
        _currentTime.value = DateTimeUtil.simpleDateFormat(0)
    }

    companion object {
        fun getViewModelFactory(
            mediaPlayerUseCase: MediaPlayerUseCase,
            timerUseCase: TimerUseCase,
            playerInteractor: PlayerInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MediaPlayViewModel(
                    mediaPlayerUseCase,
                    timerUseCase,
                    playerInteractor
                )
            }
        }
    }

    override fun updateTime(currentPositionTime: Int) {
        Log.d("MyLog", "currentPosition SUM: $currentPositionTime")
        val formattedTime = DateTimeUtil.simpleDateFormat(currentPositionTime.toLong())
        _currentTime.postValue(formattedTime)
    }

    override fun onTimeUpdate(currentPosition: Int) {
        updateTime(currentPosition)
    }

    fun stopPlayback() {
        mediaPlayerUseCase.pause()
    }

}