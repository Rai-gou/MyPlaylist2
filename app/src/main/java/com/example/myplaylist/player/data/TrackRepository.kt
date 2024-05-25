package com.example.myplaylist.player.data

import android.util.Log
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.ResponseClass
import com.example.myplaylist.search.data.TrackDataSource
import retrofit2.Response

class TrackRepository(
    private val trackDataSource: TrackDataSource
) {
    suspend fun searchTracks(track: Track): Response<ResponseClass> {
        Log.d("MyLog", "query: $track")
        return trackDataSource.searchTracks(track)
    }

}