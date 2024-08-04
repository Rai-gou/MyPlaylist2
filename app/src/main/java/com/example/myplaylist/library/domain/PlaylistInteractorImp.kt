package com.example.myplaylist.library.domain

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.myplaylist.library.data.PlaylistRepository
import com.example.myplaylist.library.db.PlaylistEntity

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override fun getAllPlaylists(): LiveData<List<PlaylistEntity>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun getAllPlaylistsMediaPlay(): List<PlaylistEntity> {
        return playlistRepository.getAllPlaylistsMediaPlay()
    }

    override suspend fun createPlaylist(name: String, imageUri: Uri?) {
        playlistRepository.createPlaylist(name, imageUri)
    }

    override suspend fun addTrackToPlaylistTrackList(playlistId: String, trackId: String):Boolean {
        return playlistRepository.addTrackToPlaylistTrackList(playlistId, trackId)
    }

    override suspend fun incrementTrackCount(playlistId: String) {
        playlistRepository.incrementTrackCount(playlistId)
    }
    override suspend fun getPlaylistNameById(playlistId: String): String{
        return playlistRepository.getPlaylistNameById(playlistId)

    }
    override suspend fun insertPlaylist(playlist: PlaylistEntity) {
        playlistRepository.insertPlaylist(playlist)
    }

    override fun getPlaylist(playlistId: String): LiveData<PlaylistEntity?> {
        return playlistRepository.getPlaylist(playlistId)
    }
}