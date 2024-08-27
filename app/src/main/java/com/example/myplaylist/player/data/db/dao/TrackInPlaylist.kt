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
    @Query("DELETE FROM track_in_playlist WHERE trackId = :trackId AND NOT EXISTS (SELECT 1 FROM playlist_table WHERE playlistTrackList LIKE '%' || :trackId || '%')")
    suspend fun deleteTrackIfNotInAnyPlaylist(trackId: String)
}