package com.example.myplaylist.search.data

import android.util.Log
import com.example.myplaylist.player.model.Track
import retrofit2.Response

class RemoteTrackDataSourceImpl(private val itunesApi: ItunesApi) : RemoteTrackDataSource {

    override suspend fun searchTracks(query: String): List<Track> {
        return try {
            val response: Response<ResponseClass> = itunesApi.search(query)
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