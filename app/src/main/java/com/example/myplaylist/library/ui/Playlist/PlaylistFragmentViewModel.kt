package com.example.myplaylist.library.ui.Playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.domain.PlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _allPlaylists = MutableLiveData<List<NewPlaylist>>(emptyList())
    val allPlaylists: LiveData<List<NewPlaylist>> = _allPlaylists

    private val _playlistWithTracks = MutableLiveData<NewPlaylistWithTracks?>()
    val playlistWithTracks: LiveData<NewPlaylistWithTracks?> get() = _playlistWithTracks
    init {
        viewModelScope.launch {
            _allPlaylists.value = playlistInteractor.getAllPlaylists().first()
        }
    }

    fun refreshPlaylists() {
        viewModelScope.launch {
            val updatedPlaylists = playlistInteractor.getAllPlaylists().first()
            _allPlaylists.value = updatedPlaylists
            Log.d("PlaylistFragmentViewModel", "Playlists refreshed: ${updatedPlaylists.size}")
        }
    }
}