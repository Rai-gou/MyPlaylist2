package com.example.myplaylist.library.domain

import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks

interface OpenPlaylistInteractor {
    suspend fun getPlaylistById(playlistId: String): NewPlaylist?
    suspend fun getPlaylistWithTracks(playlistId: String): NewPlaylistWithTracks?
    suspend fun removeTrackFromPlaylist(trackId: String, playlistId: String)
    suspend fun deletePlaylist(playlistId: String)
}