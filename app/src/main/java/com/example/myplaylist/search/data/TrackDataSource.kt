package com.example.myplaylist.search.data

import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ResponseClass
import retrofit2.Response

interface TrackDataSource {
    suspend fun getTracks(query: String): Response<ResponseClass>
    suspend fun searchTracks(track: Track): Response<ResponseClass>
}
