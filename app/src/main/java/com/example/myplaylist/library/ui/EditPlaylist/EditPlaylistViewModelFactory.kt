package com.example.myplaylist.library.ui.EditPlaylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylist.library.domain.PlaylistInteractor
import com.example.myplaylist.library.ui.NewPlaylist.NewPlaylistViewModel

class EditPlaylistViewModelFactory(
    private val playlistInteractor: PlaylistInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EditPlaylistViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                EditPlaylistViewModel(playlistInteractor) as T
            }
            modelClass.isAssignableFrom(NewPlaylistViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                NewPlaylistViewModel(playlistInteractor) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}