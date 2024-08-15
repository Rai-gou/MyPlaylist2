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

    @Query("SELECT * FROM track_in_playlist WHERE trackId = :trackId")
    suspend fun getTrackInPlaylist(trackId: String): TrackInPlaylistEntity?
}