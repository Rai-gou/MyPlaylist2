package com.example.myplaylist.library.ui.Playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.domain.PlaylistInteractor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    val allPlaylists: StateFlow<List<NewPlaylist>> = playlistInteractor.getAllPlaylists()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}