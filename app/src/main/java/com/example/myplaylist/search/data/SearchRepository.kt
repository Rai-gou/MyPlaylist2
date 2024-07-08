package com.example.myplaylist.search.data

import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchTracks(track: Track): Flow<List<Track>>
}

