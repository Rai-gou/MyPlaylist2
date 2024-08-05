package com.example.myplaylist.library.ui.Selected

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylist.R
import com.example.myplaylist.player.domain.FavoritesInteractor
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.launch
class SelectedFragmentViewModel(
    private val context: Context,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val selectedFragmentLiveData = MutableLiveData<HistoryStateSelected>()
    fun getSelectedFragmentLiveData(): LiveData<HistoryStateSelected> = selectedFragmentLiveData

    fun fillData() {
        renderState(HistoryStateSelected.Loading)
        viewModelScope.launch {
            favoritesInteractor
                .historyTrackDatabase()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    fun getTrackFavoriteStatus(track: Track, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFavorite = favoritesInteractor.checkTrackIsFavorite(track)
            callback(isFavorite)
        }
    }
    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(HistoryStateSelected.Empty(context.getString(R.string.nothing)))
        } else {
            renderState(HistoryStateSelected.Content(tracks))
        }
    }

    private fun renderState(state: HistoryStateSelected) {
        selectedFragmentLiveData.postValue(state)
    }
}