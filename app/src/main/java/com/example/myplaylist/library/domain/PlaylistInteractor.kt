package com.example.myplaylist.library.domain

import android.net.Uri
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getAllPlaylistsWithTracks(): List<NewPlaylistWithTracks>
    suspend fun getPlaylistWithTracks(playlistId: String): NewPlaylistWithTracks?
    suspend fun getAllPlaylistsMediaPlay(): List<NewPlaylist>
    suspend fun createPlaylist(name: String, description: String, imageUri: Uri?)
    suspend fun addTrackToPlaylistTrackList(playlistId: String, track: Track): Boolean
    suspend fun incrementTrackCount(playlistId: String)
    suspend fun insertPlaylist(playlist: NewPlaylist)
    suspend fun getPlaylistNameById(playlistId: String): String
    suspend fun getPlaylistById(playlistId: String): NewPlaylist?
    suspend fun updatePlaylist(
        playlistId: String,
        name: String,
        description: String,
        imageUri: Uri?
    )
}