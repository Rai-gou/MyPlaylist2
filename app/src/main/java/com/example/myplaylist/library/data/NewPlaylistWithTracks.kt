package com.example.myplaylist.library.data

import com.example.myplaylist.player.model.Track

data class NewPlaylistWithTracks(
    val id: String,
    val name: String,
    val description: String,
    val trackList: List<Track>,
    val previewUrl: String,
    val trackCount: Int
)