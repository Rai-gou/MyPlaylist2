package com.example.myplaylist.library.ui.OpenPlaylist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.domain.OpenPlaylistInteractor
import com.example.myplaylist.library.ui.Playlist.PlaylistFragmentViewModel
import kotlinx.coroutines.launch

class OpenPlaylistViewModel(
    private val openPlaylistInteractor: OpenPlaylistInteractor,
    private val playlistFragmentViewModel: PlaylistFragmentViewModel
) : ViewModel() {

    private val _playlistWithTracks = MutableLiveData<NewPlaylistWithTracks?>()
    val playlistWithTracks: LiveData<NewPlaylistWithTracks?> get() = _playlistWithTracks

    private val _playlistNewPlaylist = MutableLiveData<NewPlaylist?>()
    val playlistNewPlaylist: LiveData<NewPlaylist?> get() = _playlistNewPlaylist

    private val _playlistImageUri = MutableLiveData<Uri?>()
    val playlistImageUri: LiveData<Uri?> get() = _playlistImageUri


    private var currentPlaylistId: String? = null

    fun loadPlaylist(playlistId: String) {
        currentPlaylistId = playlistId
        viewModelScope.launch {
            val playlistWithTracks = openPlaylistInteractor.getPlaylistWithTracks(playlistId)
            _playlistWithTracks.value = playlistWithTracks
            _playlistNewPlaylist.value = playlistWithTracks?.let {
                NewPlaylist(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    trackList = it.trackList.map { track -> track.trackId },
                    previewUrl = it.previewUrl,
                    trackCount = it.trackCount
                )
            }

            if (playlistWithTracks?.previewUrl != null) {
                val uri = Uri.parse("file://" + playlistWithTracks.previewUrl)
                _playlistImageUri.value = uri
            }
        }
    }

    fun deleteTrackFromPlaylist(trackId: String, playlistId: String) {
        viewModelScope.launch {
            openPlaylistInteractor.removeTrackFromPlaylist(trackId, playlistId)
            val updatedPlaylist = openPlaylistInteractor.getPlaylistWithTracks(playlistId)

            loadPlaylist(playlistId)

            if (updatedPlaylist != null && updatedPlaylist.trackList.isNotEmpty()) {
                _playlistWithTracks.value = updatedPlaylist
                playlistFragmentViewModel.updatePlaylist(updatedPlaylist)
            } else {
                _playlistWithTracks.value = null
            }
            updatedPlaylist?.let {
                playlistFragmentViewModel.updatePlaylist(it)

            }
        }
    }

    fun deletePlaylist(playlistId: String) {
        viewModelScope.launch {
            openPlaylistInteractor.deletePlaylist(playlistId)
            _playlistWithTracks.value = null
        }
    }
}