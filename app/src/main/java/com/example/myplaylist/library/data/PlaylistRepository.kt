package com.example.myplaylist.library.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.player.data.db.TrackEntity

interface PlaylistRepository {
    suspend fun createPlaylist(name: String, imageUri: Uri?)
    fun getAllPlaylists(): LiveData<List<PlaylistEntity>>
    suspend fun getAllPlaylistsMediaPlay(): List<PlaylistEntity>
    suspend fun addTrackToPlaylistTrackList(playlistId: String, trackId: String): Boolean
    suspend fun incrementTrackCount(playlistId: String)
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    fun getPlaylist(playlistId: String): LiveData<PlaylistEntity?>
    suspend fun getPlaylistNameById(playlistId: String): String
    fun generatePlaylistId(): String
}