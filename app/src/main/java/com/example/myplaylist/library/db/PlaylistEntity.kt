package com.example.myplaylist.library.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey
    val playlistId: String,
    val playlistName: String,
    val playlistTrackList: String,
    val previewUrlList: String,
    val trackCount: Int = 0
)