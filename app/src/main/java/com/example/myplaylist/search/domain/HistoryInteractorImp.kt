package com.example.myplaylist.search.domain

import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.HistoryRepository

class HistoryInteractorImp(private val historyRepositoryImpl: HistoryRepository): HistoryInteractor {

    override fun loadHistoryTracks(): List<Track> {
        return historyRepositoryImpl.loadHistoryTracks()
    }

    override fun saveHistoryTrack(track: Track) {
        historyRepositoryImpl.saveHistoryTrack(track)
    }

    override fun clearHistory() {
        historyRepositoryImpl.clearHistory()
    }
}