package com.example.myplaylist.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    val allPlaylists: LiveData<List<PlaylistEntity>> = playlistInteractor.getAllPlaylists()

    fun insertPlaylist(playlist: PlaylistEntity) {
        viewModelScope.launch {
            playlistInteractor.insertPlaylist(playlist)
        }
    }
    fun getPlaylist(playlistId: String): LiveData<PlaylistEntity?> {
        return playlistInteractor.getPlaylist(playlistId)
    }
}