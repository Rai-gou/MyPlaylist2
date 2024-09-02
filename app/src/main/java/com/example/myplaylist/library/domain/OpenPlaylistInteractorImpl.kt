package com.example.myplaylist.library.domain

import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.data.OpenPlaylistRepository

class OpenPlaylistInteractorImpl(
    private val openPlaylistRepository: OpenPlaylistRepository
) : OpenPlaylistInteractor {

    override suspend fun getPlaylistById(playlistId: String): NewPlaylist? {
        return openPlaylistRepository.getPlaylistById(playlistId)
    }

    override suspend fun getPlaylistWithTracks(playlistId: String): NewPlaylistWithTracks? {
        return openPlaylistRepository.getPlaylistWithTracks(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(trackId: String, playlistId: String) {
        openPlaylistRepository.removeTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun deletePlaylist(playlistId: String) {
        openPlaylistRepository.deletePlaylist(playlistId)
    }
}