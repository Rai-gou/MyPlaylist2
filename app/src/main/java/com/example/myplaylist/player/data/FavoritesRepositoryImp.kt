package com.example.myplaylist.player.data

import com.example.myplaylist.player.data.converters.TrackDbConvertor
import com.example.myplaylist.player.data.db.AppDatabase
import com.example.myplaylist.player.data.db.TrackEntity
import com.example.myplaylist.player.domain.db.FavoritesRepository
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImp(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoritesRepository {
    override fun historyTrackDatabase(): Flow<List<Track>> {
        return appDatabase.trackDao().getTrack().map { trackEntities ->
            converterFromTrackEntity(trackEntities)
        }
    }
    override suspend fun saveTracks(trackSelected: List<Track>) {
        val trackEntities = trackSelected.map { trackDbConvertor.map(it) }
        appDatabase.trackDao().insertTrack(trackEntities)
    }
    private fun converterFromTrackEntity(trackEntities: List<TrackEntity>): List<Track> {
        return trackEntities.map { trackDbConvertor.map(it) }
    }
    override suspend fun deleteTrackId(track: Track) {
        val trackId = track.trackId
        if (appDatabase.trackDao().isTrackExists(trackId) > 0) {
            appDatabase.trackDao().deleteTrackById(trackId)
        }
    }
    override suspend fun checkTrackIsFavorite(track: Track): Boolean {
        val trackId = track.trackId
        return appDatabase.trackDao().isTrackExists(trackId) > 0
    }

    override suspend fun clearHistory() {
        appDatabase.trackDao().deleteAllTracks()
    }
}