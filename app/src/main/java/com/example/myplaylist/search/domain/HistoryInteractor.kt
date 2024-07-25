package com.example.myplaylist.search.domain

import com.example.myplaylist.player.model.Track

interface HistoryInteractor {
    fun saveHistoryTrack(track: Track)
    fun loadHistoryTracks(): List<Track>
    fun clearHistory()
}