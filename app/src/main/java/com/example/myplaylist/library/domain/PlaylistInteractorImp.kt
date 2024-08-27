package com.example.myplaylist.library.domain

import android.net.Uri
import android.util.Log
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.PlaylistRepository
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override fun getAllPlaylists(): Flow<List<NewPlaylist>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun getAllPlaylistsMediaPlay(): List<NewPlaylist> {
        return playlistRepository.getAllPlaylistsMediaPlay()
    }

    override suspend fun createPlaylist(name: String, description: String, imageUri: Uri?) {
        playlistRepository.createPlaylist(name, description, imageUri)
    }

    override suspend fun addTrackToPlaylistTrackList(playlistId: String, track: Track): Boolean {
        return playlistRepository.addTrackToPlaylistTrackList(playlistId, track)
    }

    override suspend fun incrementTrackCount(playlistId: String) {
        playlistRepository.incrementTrackCount(playlistId)
    }

    override suspend fun getPlaylistNameById(playlistId: String): String {
        return playlistRepository.getPlaylistNameById(playlistId)
    }

    override suspend fun insertPlaylist(playlist: NewPlaylist) {
        playlistRepository.insertPlaylist(playlist)
    }
    override suspend fun getPlaylistById(playlistId: String): NewPlaylist? {
        return playlistRepository.getPlaylistById(playlistId)
    }
    override suspend fun updatePlaylist(playlistId: String, name: String, description: String, imageUri: Uri?) {
        Log.d("PlaylistRepositoryImpl", "updatePlaylist PlaylistInteractorImpl: $playlistId with name: $name, description: $description, imagePath: $imageUri")
        playlistRepository.updatePlaylist(playlistId, name, description, imageUri)
    }
}