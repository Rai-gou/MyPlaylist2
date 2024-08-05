package com.example.myplaylist.library.ui.Selected

import com.example.myplaylist.player.model.Track

interface HistoryStateSelected {

    object Loading : HistoryStateSelected

    data class Content(
        val trackSelected: List<Track>
    ) : HistoryStateSelected

    data class Empty(
        val message: String
    ) : HistoryStateSelected
}