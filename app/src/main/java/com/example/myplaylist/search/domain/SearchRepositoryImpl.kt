package com.example.myplaylist.search.domain

import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ResponseClass
import com.example.myplaylist.search.data.SearchRepository
import retrofit2.Response

class SearchRepositoryImpl(
    private val trackRepository: TrackRepository
) : SearchRepository {

    override suspend fun searchTracks(track: Track): Track {
        val response = trackRepository.searchTracks(track)
        return extractTrackFromResponse(response)
    }

    private fun extractTrackFromResponse(response: Response<ResponseClass>): Track {
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null && responseBody.results.isNotEmpty()) {
            return responseBody.results[0]
        } else {
            return Track("", "", "", null, "", "", "", "", "", "")
        }
    }
}