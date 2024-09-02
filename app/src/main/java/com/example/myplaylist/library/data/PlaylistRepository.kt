package com.example.myplaylist.library.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.db.TrackInPlaylistEntity
import com.example.myplaylist.player.data.db.TrackEntity
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getAllPlaylistsWithTracks(): List<NewPlaylistWithTracks>
    suspend fun createPlaylist(name: String, description:String, imageUri: Uri?)
    suspend fun getPlaylistWithTracks(playlistId: String): NewPlaylistWithTracks?
    suspend fun getAllPlaylistsMediaPlay(): List<NewPlaylist>
    suspend fun addTrackToPlaylistTrackList(playlistId: String, track: Track): Boolean
    suspend fun incrementTrackCount(playlistId: String)
    suspend fun insertPlaylist(playlist: NewPlaylist)
    suspend fun getPlaylistNameById(playlistId: String): String
    fun generatePlaylistId(): String
    suspend fun getPlaylistById(playlistId: String): NewPlaylist?
    suspend fun updatePlaylist(playlistId: String, name: String, description: String, imageUri: Uri?)
    fun playlistDescription(): String

}