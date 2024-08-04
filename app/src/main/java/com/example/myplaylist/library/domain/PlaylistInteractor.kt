package com.example.myplaylist.library.domain

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.player.data.db.TrackEntity

interface PlaylistInteractor {
    fun getAllPlaylists(): LiveData<List<PlaylistEntity>>
    suspend fun getAllPlaylistsMediaPlay(): List<PlaylistEntity>
    suspend fun createPlaylist(name: String, imageUri: Uri?)
    suspend fun addTrackToPlaylistTrackList(playlistId: String, trackId: String): Boolean
    suspend fun incrementTrackCount(playlistId: String)
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    suspend fun getPlaylistNameById(playlistId: String): String
    fun getPlaylist(playlistId: String): LiveData<PlaylistEntity?>
}