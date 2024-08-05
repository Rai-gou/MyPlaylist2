package com.example.myplaylist.library.domain

import android.net.Uri
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.PlaylistRepository
import com.example.myplaylist.library.db.TrackInPlaylistEntity
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

    override suspend fun createPlaylist(name: String, imageUri: Uri?) {
        playlistRepository.createPlaylist(name, imageUri)
    }

    override suspend fun addTrackToPlaylistTrackList(playlistId: String, trackId: TrackInPlaylistEntity): Boolean {
        return playlistRepository.addTrackToPlaylistTrackList(playlistId, trackId)
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
}