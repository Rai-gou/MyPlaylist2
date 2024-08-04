package com.example.myplaylist.library.ui

import android.net.Uri

data class ScreenStateNewPlaylist(
    val playlistName: String = "",
    val playlistDescription: String = "",
    val playlistImageUri: Uri? = null,
    val isDataChanged: Boolean = false
)