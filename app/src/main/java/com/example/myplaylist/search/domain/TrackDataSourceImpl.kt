package com.example.myplaylist.search.domain

import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.ResponseClass
import com.example.myplaylist.search.data.RemoteTrackDataSourceImpl
import com.example.myplaylist.search.data.TrackDataSource
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

class TrackDataSourceImpl(private val itunesApi: RemoteTrackDataSourceImpl) : TrackDataSource {

    override suspend fun searchTracks(track: Track): Response<ResponseClass> {
        val encodedQuery = "\"${track.trackName}\""
        return try {
            val trackList: List<Track> = itunesApi.searchTracks(encodedQuery)
            val responseClass =
                ResponseClass(trackList.size, trackList, true, "Successful response")
            Response.success(responseClass)
        } catch (e: Exception) {
            Response.error(500, ResponseBody.create(MediaType.parse("application/json"), ""))
        }
    }

    override suspend fun getTracks(query: String): Response<ResponseClass> {
        return Response.error(500, ResponseBody.create(MediaType.parse("application/json"), ""))
    }
}