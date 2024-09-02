package com.example.myplaylist.library.ui.EditPlaylist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.domain.PlaylistInteractor
import com.example.myplaylist.library.ui.NewPlaylist.NewPlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    playlistInteractor: PlaylistInteractor
) : NewPlaylistViewModel(playlistInteractor) {

    override val _playlistId = MutableLiveData<String>()
    override val playlistId: LiveData<String> get() = _playlistId

    private val _playlistWithTracks = MutableLiveData<NewPlaylistWithTracks?>()
    val playlistWithTracks: LiveData<NewPlaylistWithTracks?> get() = _playlistWithTracks

    private val _playlistNewPlaylist = MutableLiveData<NewPlaylist?>()
    val playlistNewPlaylist: LiveData<NewPlaylist?> get() = _playlistNewPlaylist

    private var currentPlaylistId: String? = null

    fun loadPlaylist(playlistId: String) {
        _playlistId.value = playlistId
        currentPlaylistId = playlistId
        viewModelScope.launch {
            val playlistWithTracks = playlistInteractor.getPlaylistWithTracks(playlistId)
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

    fun savePlaylist(onComplete: () -> Unit) {
        val playlistId = _playlistId.value ?: "defaultId"
        val name = _playlistName.value ?: "Unnamed Playlist"
        val description = _playlistDescription.value ?: "No Description"
        val imageUri = _playlistImageUri.value
        viewModelScope.launch {
            try {
                playlistInteractor.updatePlaylist(playlistId, name, description, imageUri)
                onComplete()
            } catch (e: Exception) {
                Log.e("EditPlaylistViewModel", "Error saving playlist: ${e.message}", e)
            }
        }
    }
}