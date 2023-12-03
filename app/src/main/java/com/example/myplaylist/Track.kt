package com.example.myplaylist

data class Track(
    val trackName: String,
    val artistName: String,
    val trackId: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)