package com.example.myplaylist.search.domain

import android.os.Handler
import android.os.Looper
import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
open class SearchInteractor(
    val searchRepository: SearchRepository,
    private val trackRepository: TrackRepository,
) {

    private val handler = Handler(Looper.getMainLooper())
    open suspend fun searchTracks(track: Track): Flow<List<Track>> {
        return trackRepository.searchTracks(track)
            .map { response ->
                if (response.isSuccessful && response.body()?.results?.isNotEmpty() == true) {
                    response.body()?.results ?: emptyList()
                } else {
                    emptyList()
                }
            }
    }


    fun onItemClick(onTrackSaved: (Boolean) -> Unit) {
        if (clickDebounce()) {
            onTrackSaved(true)
        } else {
            onTrackSaved(false)
        }
    }

    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }
}