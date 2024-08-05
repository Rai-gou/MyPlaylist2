package com.example.myplaylist.library.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.db.TrackInPlaylistEntity
import com.example.myplaylist.player.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(name: String, imageUri: Uri?)
    fun getAllPlaylists(): Flow<List<NewPlaylist>>
    suspend fun getAllPlaylistsMediaPlay(): List<NewPlaylist>
    suspend fun addTrackToPlaylistTrackList(playlistId: String, track: TrackInPlaylistEntity): Boolean
    suspend fun incrementTrackCount(playlistId: String)
    suspend fun insertPlaylist(playlist: NewPlaylist)
    suspend fun getPlaylistNameById(playlistId: String): String
    fun generatePlaylistId(): String
}