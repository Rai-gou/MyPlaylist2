package com.example.myplaylist.search.data

import com.example.myplaylist.player.model.Track

interface HistoryRepository {
    fun saveHistoryTrack(track: Track)
    fun loadHistoryTracks(): List<Track>
    fun clearHistory()
}