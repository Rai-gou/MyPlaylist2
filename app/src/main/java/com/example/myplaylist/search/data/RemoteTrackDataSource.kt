package com.example.myplaylist.search.data

import com.example.myplaylist.player.model.Track

interface RemoteTrackDataSource {
    suspend fun searchTracks(query: String): List<Track>
}
