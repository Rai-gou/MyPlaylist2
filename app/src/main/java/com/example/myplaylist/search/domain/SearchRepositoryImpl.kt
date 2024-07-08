package com.example.myplaylist.search.domain

import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.ResponseClass
import com.example.myplaylist.search.data.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class SearchRepositoryImpl(
    private val trackRepository: TrackRepository
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