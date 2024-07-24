package com.example.myplaylist.player.domain

import com.example.myplaylist.player.domain.db.HistoryRepositoryDatabase
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

class HistoryInteractorDatabaseImp(private val historyRepositoryDatabase: HistoryRepositoryDatabase): HistoryInteractorDatabase {
    override suspend fun historyTrackDatabase(): Flow<List<Track>> {
        return historyRepositoryDatabase.historyTrackDatabase()
    }
}