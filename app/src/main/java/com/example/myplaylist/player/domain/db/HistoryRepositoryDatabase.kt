package com.example.myplaylist.player.domain.db

import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

interface HistoryRepositoryDatabase {
    suspend fun historyTrackDatabase(): Flow<List<Track>>
    suspend fun saveTracks(trackSelected: List<Track>)
    suspend fun clearHistory()
    suspend fun deleteTrackId(track: Track)
    suspend fun checkTrackIsFavorite(track: Track): Boolean
}