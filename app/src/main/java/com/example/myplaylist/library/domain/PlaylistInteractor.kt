package com.example.myplaylist.library.domain

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.db.TrackInPlaylistEntity
import com.example.myplaylist.player.data.db.TrackEntity
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getAllPlaylists(): Flow<List<NewPlaylist>>
    suspend fun getAllPlaylistsMediaPlay(): List<NewPlaylist>
    suspend fun createPlaylist(name: String, imageUri: Uri?)
    suspend fun addTrackToPlaylistTrackList(playlistId: String, track: Track): Boolean
    suspend fun incrementTrackCount(playlistId: String)
    suspend fun insertPlaylist(playlist: NewPlaylist)
    suspend fun getPlaylistNameById(playlistId: String): String
}