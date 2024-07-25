package com.example.myplaylist.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myplaylist.player.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: List<TrackEntity>)

    @Query("SELECT * FROM track_table ORDER BY addedTimestamp DESC")
    fun getTrack(): Flow<List<TrackEntity>>

    @Query("DELETE FROM track_table")
    suspend fun deleteAllTracks()

    @Query("SELECT COUNT(*) FROM track_table WHERE trackId = :trackId")
    suspend fun isTrackExists(trackId: String): Int

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: String)
}