package com.example.myplaylist.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myplaylist.library.db.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylistsMediaPlay(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistSync(playlistId: String): PlaylistEntity?

    @Query("UPDATE playlist_table SET playlistTrackList = :trackId WHERE playlistId = :playlistId")
    suspend fun addTrackToPlaylistTrackList(playlistId: String, trackId: String)

    @Query("UPDATE playlist_table SET trackCount = trackCount + 1 WHERE playlistId = :playlistId")
    suspend fun incrementTrackCount(playlistId: String)

    @Query("SELECT playlistName FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistName(playlistId: String): String
}