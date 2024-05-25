package com.example.myplaylist.search.domain

import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.ResponseClass
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.SearchRepository
import retrofit2.Response

class SearchInteractorImpl(
    searchRepository: SearchRepository,
    historyRepository: HistoryRepository,
    private val trackRepository: TrackRepository
) : SearchInteractor(searchRepository, historyRepository, trackRepository) {

    override fun loadSomeData(onComplete: () -> Unit) {
        onComplete.invoke()
    }

    override suspend fun searchTracks(track: Track): Response<ResponseClass> {
        return trackRepository.searchTracks(track)
    }
}