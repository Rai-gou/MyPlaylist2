package com.example.myplaylist.player.ui

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.R
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.domain.PlaylistInteractor
import com.example.myplaylist.player.data.converters.TrackInPlaylistConvertor
import com.example.myplaylist.player.domain.FavoritesInteractor
import com.example.myplaylist.player.domain.PlayerInteractor
import com.example.myplaylist.player.domain.PlayerInteractorImpl
import com.example.myplaylist.player.domain.PlayerStateChangeListener
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
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val trackInPlaylistConvertor: TrackInPlaylistConvertor
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

    private val _allPlaylists = MutableStateFlow<List<NewPlaylist>>(emptyList())
    val allPlaylists: StateFlow<List<NewPlaylist>> get() = _allPlaylists.asStateFlow()

    private val _isTrackFavorite = MutableLiveData<Boolean>()
    val isTrackFavorite: LiveData<Boolean> get() = _isTrackFavorite

    // Добавляем новое LiveData для статуса добавления трека
    private val _trackAddStatus = MutableLiveData<Pair<Boolean, String>>()
    val trackAddStatus: LiveData<Pair<Boolean, String>> get() = _trackAddStatus

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
        viewModelScope.launch {
            loadAllPlaylists()
        }
    }

    private suspend fun loadAllPlaylists() {
        val playlists: List<NewPlaylist> = playlistInteractor.getAllPlaylistsMediaPlay()
        _allPlaylists.value = playlists
    }

    fun addTrackToPlaylist(playlistId: String, track: Track) {
        viewModelScope.launch {
            // Преобразование Track в TrackInPlaylistEntity
            val trackInPlaylistEntity = trackInPlaylistConvertor.map(track)
            // Добавление трека в плейлист
            val wasAdded = playlistInteractor.addTrackToPlaylistTrackList(playlistId, trackInPlaylistEntity)
            // Получение имени плейлиста
            val playlistName = playlistInteractor.getPlaylistNameById(playlistId)
            // Обновление состояния
            _trackAddStatus.value = Pair(wasAdded, playlistName)
            if (wasAdded) {
                loadAllPlaylists()
            }
        }
    }

    fun refreshPlaylists() {
        viewModelScope.launch {
            loadAllPlaylists()
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
            favoritesInteractor.saveTrack(track)
            checkTrackIsFavorite(track)
        }
    }

    fun deleteTrackOnFavorite(track: Track) {
        viewModelScope.launch {
            favoritesInteractor.deleteTrack(track)
            checkTrackIsFavorite(track)
        }
    }

    fun checkTrackIsFavorite(track: Track) {
        viewModelScope.launch {
            _isTrackFavorite.value = favoritesInteractor.checkTrackIsFavorite(track)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            favoritesInteractor.clearHistory()
        }
    }
}