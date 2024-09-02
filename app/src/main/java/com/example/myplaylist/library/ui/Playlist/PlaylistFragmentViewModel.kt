package com.example.myplaylist.library.ui.Playlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.domain.PlaylistInteractor
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _allPlaylists = MutableLiveData<List<NewPlaylistWithTracks>>(emptyList())
    val allPlaylists: LiveData<List<NewPlaylistWithTracks>> = _allPlaylists

    private val _playlistWithTracks = MutableLiveData<NewPlaylistWithTracks?>()
    val playlistWithTracks: LiveData<NewPlaylistWithTracks?> get() = _playlistWithTracks

    init {
        viewModelScope.launch {
            _allPlaylists.value = playlistInteractor.getAllPlaylistsWithTracks()

        }
    }

    fun refreshPlaylists() {
        viewModelScope.launch {
            val updatedPlaylists = playlistInteractor.getAllPlaylistsWithTracks()
            _allPlaylists.value = updatedPlaylists
        }
    }

    fun updatePlaylist(updatedPlaylist: NewPlaylistWithTracks) {
        _allPlaylists.value = _allPlaylists.value?.map { playlist ->
            if (playlist.id == updatedPlaylist.id) {
                updatedPlaylist
            } else {
                playlist
            }
        }
    }
}