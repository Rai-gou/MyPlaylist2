package com.example.myplaylist.player.data

import android.util.Log
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.ResponseClass
import com.example.myplaylist.search.data.TrackDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class TrackRepository(
    private val trackDataSource: TrackDataSource
) {
    suspend fun searchTracks(track: Track): Flow<Response<ResponseClass>> {
        return flow {
            val response = trackDataSource.searchTracks(track)
            emit(response)
        }
    }
}