package com.example.myplaylist.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.ResponseClass
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.NetworkUtils
import com.example.myplaylist.search.domain.SearchInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var tracksLiveData = MutableLiveData<List<Track>>()
    private var lastQuery: String = ""
    var searchJob: Job? = null
    private val progressBar = MutableLiveData(false)
    private val errorActivity = MutableLiveData(false)
    private val nothingActivity = MutableLiveData(false)
    private val recyclerView = MutableLiveData(true)
    private val clearIcon = MutableLiveData(false)
    private val buttonClearHistory = MutableLiveData(false)
    private val yourHistory = MutableLiveData(false)


    fun getTracksLiveData(): LiveData<List<Track>> = tracksLiveData
    fun progressBarVisible(): LiveData<Boolean> = progressBar
    fun errorActivityVisible(): LiveData<Boolean> = errorActivity
    fun nothingActivityVisible(): LiveData<Boolean> = nothingActivity
    fun recyclerViewVisible(): LiveData<Boolean> = recyclerView
    fun clearIcon(): LiveData<Boolean> = clearIcon
    fun buttonClearHistory(): LiveData<Boolean> = buttonClearHistory
    fun yourHistory(): LiveData<Boolean> = yourHistory

    fun performSearch(query: String, networkUtils: NetworkUtils) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isNotEmpty()) {
            val track = Track(
                trackName = trimmedQuery,
                artistName = "",
                trackId = "",
                trackTimeMillis = null,
                artworkUrl100 = "",
                previewUrl = "",
                collectionName = "",
                releaseDate = "",
                primaryGenreName = "",
                country = ""
            )
            searchTracks(track, networkUtils)
        } else {
            loadHistoryTracks()
        }
    }

    fun searchTracks(track: Track, networkUtils: NetworkUtils) {

        val query = track.trackName
        lastQuery = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isEmpty()) {
                return@launch
            }
            val isConnected = networkUtils.isNetworkAvailable()
            progressBar.postValue(true)
            recyclerView.postValue(false)
            nothingActivity.postValue(false)
            errorActivity.postValue(false)
            buttonClearHistory.postValue(false)
            yourHistory.postValue(false)
            if (!isConnected) {
                errorActivity.postValue(true)
                clearIcon.postValue(true)
                progressBar.postValue(false)
            } else {
                try {
                    delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
                    val response: Response<ResponseClass> = searchInteractor.searchTracks(track)
                    if (response.isSuccessful) {
                        Log.d("response result", "result")
                        val tracks: List<Track> = response.body()?.results ?: emptyList()
                        Log.d("MyLog", "After tracks: $tracks")
                        tracksLiveData.postValue(tracks)
                        if (tracks.isEmpty()) {
                            Log.d("MyLog", "пустой запрос")
                            nothingActivity.postValue(true)
                            clearIcon.postValue(true)
                        } else {
                            nothingActivity.postValue(false)
                        }
                        Log.d("MyLog", "response.code: ${response.code()}")
                    } else {
                        error("response error")
                    }
                } finally {
                    //loadingLiveData.postValue(false)
                    progressBar.postValue(false)
                    recyclerView.postValue(true)
                    errorActivity.postValue(false)
                    clearIcon.postValue(true)
                }
            }
        }
    }

    fun loadHistoryTracks() {
        viewModelScope.launch {
            Log.d("MyLog", "historyRepository123: ${historyRepository.loadHistoryTracks()}")
            val historyTracks: List<Track> = historyRepository.loadHistoryTracks()
            Log.d("MyLog", "historyList123: $historyTracks")
            tracksLiveData.value = historyTracks
            handleHistoryTracks(historyTracks)
        }
    }

    fun handleHistoryTracks(historyTracks: List<Track>) {
        if (historyTracks.isEmpty()) {
            buttonClearHistory.postValue(false)
            yourHistory.postValue(false)
            clearIcon.postValue(false)
        } else {
            buttonClearHistory.postValue(true)
            yourHistory.postValue(true)
            clearIcon.postValue(false)
        }
    }

    fun clearHistory() {
        historyRepository.clearHistory()
        loadHistoryTracks()
    }
}