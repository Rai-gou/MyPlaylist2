package com.example.myplaylist.library.data

interface OpenPlaylistRepository {
    suspend fun getPlaylistById(playlistId: String): NewPlaylist?
    suspend fun getPlaylistWithTracks(playlistId: String): NewPlaylistWithTracks?
    suspend fun removeTrackFromPlaylist(trackId: String, playlistId: String)
    suspend fun deletePlaylist(playlistId: String)
}