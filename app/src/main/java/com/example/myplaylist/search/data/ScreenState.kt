package com.example.myplaylist.search.data

import com.example.myplaylist.player.model.Track

data class ScreenState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isNothing: Boolean = false,
    val isRecyclerViewVisible: Boolean = true,
    val isClearIconVisible: Boolean = false,
    val isButtonClearHistoryVisible: Boolean = false,
    val isYourHistoryVisible: Boolean = false,
    val tracks: List<Track> = emptyList()
)