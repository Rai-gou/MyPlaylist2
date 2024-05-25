package com.example.myplaylist.search.data

import com.example.myplaylist.player.model.Track

data class ResponseClass(
    val resultCount: Int,
    val results: List<Track>,
    val isSuccessful: Boolean,
    val body: String
) {
    constructor() : this(0, emptyList(), false, "")
}