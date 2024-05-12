package com.example.myplaylist.search.data

import android.util.Log
import com.example.myplaylist.player.data.RemoteTrackDataSource
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ResponseClass
import retrofit2.Response

class RemoteTrackDataSourceImpl(private val itunesApi: ItunesApi) : RemoteTrackDataSource {

    override suspend fun searchTracks(query: String): List<Track> {
        return try {
            Log.d("MyLog", "After encodedQuery: $query")
            val response: Response<ResponseClass> = itunesApi.search(query)
            Log.d("MyLog", "After query: $response")
            if (response.isSuccessful) {
                val responseBody = response.body()
                responseBody?.results ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}