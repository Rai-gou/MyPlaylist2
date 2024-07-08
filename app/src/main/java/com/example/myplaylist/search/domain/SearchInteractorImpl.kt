package com.example.myplaylist.search.domain

import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

class SearchInteractorImpl(
    searchRepository: SearchRepository,
    historyRepository: HistoryRepository,
    private val trackRepository: TrackRepository,
) : SearchInteractor(searchRepository, historyRepository, trackRepository) {

    override suspend fun searchTracks(track: Track): Flow<List<Track>> {
        return searchRepository.searchTracks(track)
    }
}