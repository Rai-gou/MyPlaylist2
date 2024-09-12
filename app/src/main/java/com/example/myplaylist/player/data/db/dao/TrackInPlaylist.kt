package com.example.myplaylist.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myplaylist.library.db.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(trackInPlaylist: TrackInPlaylistEntity)
    @Query("SELECT * FROM track_in_playlist WHERE trackId IN (:trackIds)")
    suspend fun getTracksInPlaylist(trackIds: List<String>): List<TrackInPlaylistEntity>
    @Query("DELETE FROM track_in_playlist WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)
    @Query("SELECT * FROM track_in_playlist")
    suspend fun getAllTracksInPlaylist(): List<TrackInPlaylistEntity>
}