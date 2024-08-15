package com.example.myplaylist.library.data

data class NewPlaylist(
    val id: String,
    val name: String,
    val trackList: List<String>,
    val previewUrl: String,
    val trackCount: Int
)
