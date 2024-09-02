package com.example.myplaylist.library.domain

import android.net.Uri
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.data.PlaylistRepository
import com.example.myplaylist.player.model.Track

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun getAllPlaylistsWithTracks(): List<NewPlaylistWithTracks> {
        return playlistRepository.getAllPlaylistsWithTracks()
    }

    override suspend fun getPlaylistWithTracks(playlistId: String): NewPlaylistWithTracks? {
        return playlistRepository.getPlaylistWithTracks(playlistId)
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

    override suspend fun updatePlaylist(
        playlistId: String,
        name: String,
        description: String,
        imageUri: Uri?
    ) {
        playlistRepository.updatePlaylist(playlistId, name, description, imageUri)
    }
}