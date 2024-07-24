package com.example.myplaylist.search.domain

import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.data.converters.TrackDbConvertor
import com.example.myplaylist.player.data.db.AppDatabase
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val trackRepository: TrackRepository,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : SearchRepository {

    override suspend fun searchTracks(track: Track): Flow<List<Track>> {
        return flow {
            trackRepository.searchTracks(track)
                .collect { response ->
                    if (response.isSuccessful && response.body()?.results?.isNotEmpty() == true) {
                        emit(response.body()?.results ?: emptyList())
                    } else {
                        emit(emptyList())
                    }
                }
        }
    }
}