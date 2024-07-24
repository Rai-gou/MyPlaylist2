package com.example.myplaylist.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.NetworkUtils
import com.example.myplaylist.search.data.ScreenState
import com.example.myplaylist.search.domain.SearchInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
const val CLICK_DELAY_MILLIS = 1500L
class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyRepositoryImpl: HistoryRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    var searchJob: Job? = null
    private var isClickInProgress = false
    private val loadingLiveData = MutableLiveData(ScreenState())
    val getLoadingLiveData: LiveData<ScreenState> get() = loadingLiveData

    fun performSearch(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isNotEmpty()) {
            val track = Track(
                trackId = "",
                trackName = trimmedQuery,
                artistName = "",
                trackTimeMillis = null,
                artworkUrl100 = "",
                previewUrl = "",
                collectionName = "",
                releaseDate = "",
                primaryGenreName = "",
                country = "",
                addedTimestamp = null,
            )
            searchTracks(track)
        } else {
            loadHistoryTracks()
        }
    }

    private fun searchTracks(track: Track) {
        val query = track.trackName
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isEmpty()) {
                return@launch
            }
            val isConnected = networkUtils.isNetworkAvailable()
            updateScreenState(
                isLoading = true,
                isRecyclerViewVisible = false,
                isNothing = false,
                isError = false,
                isButtonClearHistoryVisible = false,
                isYourHistoryVisible = false
            )
            if (!isConnected) {
                updateScreenState(
                    isLoading = false,
                    isClearIconVisible = true,
                    isError = true
                )
            } else {
                try {
                    delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
                    searchInteractor.searchTracks(track).collect { trackList ->
                        updateScreenState(tracks = trackList)
                        if (trackList.isEmpty()) {
                            updateScreenState(
                                isNothing = true,
                                isClearIconVisible = true
                            )
                        } else {
                            updateScreenState(
                                isNothing = false,
                                isClearIconVisible = true,
                                tracks = trackList
                            )
                        }
                    }
                } finally {
                    updateScreenState(
                        isLoading = false,
                        isRecyclerViewVisible = true,
                        isError = false,
                        isClearIconVisible = true
                    )
                    Log.d("MyLog", "isRecyclerViewVisible true")
                }
            }
        }
    }

    fun loadHistoryTracks() {
        viewModelScope.launch {
            val historyTracks: List<Track> = historyRepositoryImpl.loadHistoryTracks()
            Log.d("MyLog", "loadHistoryTracks: $historyTracks")
            updateScreenState(tracks = historyTracks)
            handleHistoryTracks(historyTracks)
        }
    }

    private fun handleHistoryTracks(historyTracks: List<Track>) {
        if (historyTracks.isEmpty()) {
            updateScreenState(
                isButtonClearHistoryVisible = false,
                isYourHistoryVisible = false,
                isClearIconVisible = false,
                isNothing = false,
                isError = false
            )
            Log.d("MyLog", "loadHistoryTracks: handleHistoryTracks")
        } else {
            updateScreenState(
                isButtonClearHistoryVisible = true,
                isYourHistoryVisible = true,
                isClearIconVisible = false,
                isNothing = false,
                isError = false,
                isRecyclerViewVisible = true
            )
            Log.d("MyLog", "loadHistoryTracks:  no handleHistoryTracks $historyTracks")
        }
    }

    fun onItemClick(track: Track, callback: (Boolean) -> Unit) {
        if (isClickInProgress) return
        isClickInProgress = true
        viewModelScope.launch {
            searchInteractor.onItemClick { trackSaved ->
                callback(trackSaved)
                if (trackSaved) {
                    historyRepositoryImpl.saveHistoryTrack(track)
                }
            }
            delay(CLICK_DELAY_MILLIS)
            isClickInProgress = false
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepositoryImpl.clearHistory()
            loadHistoryTracks()
        }
    }

    private fun updateScreenState(
        isLoading: Boolean? = null,
        isError: Boolean? = null,
        isNothing: Boolean? = null,
        isRecyclerViewVisible: Boolean? = null,
        isClearIconVisible: Boolean? = null,
        isButtonClearHistoryVisible: Boolean? = null,
        isYourHistoryVisible: Boolean? = null,
        tracks: List<Track>? = null
    ) {
        val currentState = loadingLiveData.value ?: ScreenState()
        loadingLiveData.value = currentState.copy(
            isLoading = isLoading ?: currentState.isLoading,
            isError = isError ?: currentState.isError,
            isNothing = isNothing ?: currentState.isNothing,
            isRecyclerViewVisible = isRecyclerViewVisible ?: currentState.isRecyclerViewVisible,
            isClearIconVisible = isClearIconVisible ?: currentState.isClearIconVisible,
            isButtonClearHistoryVisible = isButtonClearHistoryVisible ?: currentState.isButtonClearHistoryVisible,
            isYourHistoryVisible = isYourHistoryVisible ?: currentState.isYourHistoryVisible,
            tracks = tracks ?: currentState.tracks
        )
    }
}