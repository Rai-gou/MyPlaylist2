package com.example.myplaylist.library.ui.EditPlaylist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.library.domain.PlaylistInteractor
import com.example.myplaylist.library.ui.NewPlaylist.NewPlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    playlistInteractor: PlaylistInteractor
) : NewPlaylistViewModel(playlistInteractor) {

    override val _playlistId = MutableLiveData<String>()
    override val playlistId: LiveData<String> get() = _playlistId

    fun loadPlaylist(playlistId: String) {
        _playlistId.value = playlistId
        viewModelScope.launch {
            try {
                val playlist = playlistInteractor.getPlaylistById(playlistId)
                playlist?.let {
                    _playlistName.value = it.name
                    _playlistDescription.value = it.description
                    // Ensure URI is parsed and set correctly
                    _playlistImageUri.value = it.previewUrl?.takeIf { url -> url.isNotEmpty() }?.let { url -> Uri.parse(url) }
                }
            } catch (e: Exception) {
                Log.e("EditPlaylistViewModel", "Error loading playlist: ${e.message}", e)
            }
        }
    }

    fun savePlaylist(onComplete: () -> Unit) {
        val playlistId = _playlistId.value ?: return
        val name = _playlistName.value ?: return
        val description = _playlistDescription.value ?: return
        val imageUri = _playlistImageUri.value

        Log.d("EditPlaylistViewModel", "Saving playlist with ID: $playlistId")

        viewModelScope.launch {
            try {
                playlistInteractor.updatePlaylist(playlistId, name, description, imageUri)
                Log.d("EditPlaylistViewModel", "Playlist updated successfully")
                onComplete()
            } catch (e: Exception) {
                Log.e("EditPlaylistViewModel", "Error saving playlist: ${e.message}", e)
                // Handle error (e.g., show error message to user)
            }
        }
    }
}