package com.example.myplaylist.player.domain

import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

interface HistoryInteractorDatabase {
    suspend fun historyTrackDatabase(): Flow<List<Track>>
}