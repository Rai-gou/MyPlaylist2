package com.example.myplaylist.library.data

import com.example.myplaylist.player.model.Track

data class ScreenStateSelected (
    val isRecyclerViewVisible: Boolean = true,
    val tracks: List<Track> = emptyList()
)