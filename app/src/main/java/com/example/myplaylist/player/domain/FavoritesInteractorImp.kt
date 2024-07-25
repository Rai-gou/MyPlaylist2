package com.example.myplaylist.player.domain

import com.example.myplaylist.player.domain.db.FavoritesRepository
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImp(private val favoritesRepository: FavoritesRepository): FavoritesInteractor {
    override suspend fun historyTrackDatabase(): Flow<List<Track>> {
        return favoritesRepository.historyTrackDatabase()
    }


    override suspend fun saveTrack(track: Track) {
        favoritesRepository.saveTracks(listOf(track))
    }

    override suspend fun deleteTrack(track: Track) {
        favoritesRepository.deleteTrackId(track)
    }

    override suspend fun checkTrackIsFavorite(track: Track): Boolean {
        return favoritesRepository.checkTrackIsFavorite(track)
    }

    override suspend fun clearHistory() {
        favoritesRepository.clearHistory()
    }
}