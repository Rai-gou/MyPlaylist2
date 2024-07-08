package com.example.myplaylist.search.data

import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TrackDataSource {
    suspend fun searchTracks(track: Track): Response<ResponseClass>
}