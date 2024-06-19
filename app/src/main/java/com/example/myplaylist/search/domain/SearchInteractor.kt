package com.example.myplaylist.search.domain

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.ResponseClass
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.SearchRepository
import retrofit2.Response

private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L

open class SearchInteractor(
    private val searchRepository: SearchRepository,
    private val historyRepository: HistoryRepository,
    private val trackRepository: TrackRepository
) {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    open fun loadSomeData(onComplete: () -> Unit) {
        onComplete.invoke()
    }

    open suspend fun searchTracks(track: Track): Response<ResponseClass> {
        return trackRepository.searchTracks(track)
    }

    suspend fun onItemClick(track: Track, onTrackSaved: (Boolean) -> Unit) {
        if (clickDebounce()) {
            searchRepository.searchTracks(track)
            historyRepository.loadHistoryTracks()
            onTrackSaved(true)
        } else {
            onTrackSaved(false)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }
}