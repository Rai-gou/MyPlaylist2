package com.example.myplaylist.library.ui.NewPlaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylist.library.domain.PlaylistInteractor

class NewPlaylistViewModelFactory(
    private val playlistInteractor: PlaylistInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewPlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewPlaylistViewModel(playlistInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}