package com.example.myplaylist.search.domain

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.ResponseClass
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response

private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
open class SearchInteractor(
    val searchRepository: SearchRepository,
    private val historyRepository: HistoryRepository,
    private val trackRepository: TrackRepository,
) {

    private lateinit var coroutineScope: CoroutineScope

    fun initCoroutineScope(coroutineScope: CoroutineScope) {
        this.coroutineScope = coroutineScope
    }

    private var isClickAllowed = true

    open fun loadSomeData(onComplete: () -> Unit) {
        onComplete.invoke()
    }

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

    suspend fun onItemClick(track: Track, onTrackSaved: (Boolean) -> Unit) {
        if (clickDebounce()) {
            searchRepository.searchTracks(track)
            historyRepository.loadHistoryTracks()
            onTrackSaved(true)
        } else {
            onTrackSaved(false)
        }
    }

    private suspend fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            coroutineScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }
}