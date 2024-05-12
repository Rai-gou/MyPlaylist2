package com.example.myplaylist.search.data

import com.example.myplaylist.player.model.Track

interface SearchRepository {
    suspend fun searchTracks(track: Track): Track
}