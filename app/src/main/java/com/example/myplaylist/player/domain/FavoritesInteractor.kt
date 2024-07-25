package com.example.myplaylist.player.domain

import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun historyTrackDatabase(): Flow<List<Track>>
    suspend fun saveTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun checkTrackIsFavorite(track: Track): Boolean
    suspend fun clearHistory()
}