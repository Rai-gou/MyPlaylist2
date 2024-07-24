package com.example.myplaylist.search.domain

import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    searchRepository: SearchRepository,
    private val trackRepository: TrackRepository,
) : SearchInteractor(searchRepository, trackRepository) {

    override suspend fun searchTracks(track: Track): Flow<List<Track>> {
        return searchRepository.searchTracks(track)
    }
}