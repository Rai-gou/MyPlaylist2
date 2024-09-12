package com.example.myplaylist.library.data.converters

import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.db.PlaylistEntity

class PlaylistDbConverter {

    fun mapToDomain(entity: PlaylistEntity): NewPlaylist {
        return NewPlaylist(
            id = entity.playlistId,
            name = entity.playlistName,
            description = entity.playlistDescription,
            trackList = entity.playlistTrackList.split(",").filter { it.isNotEmpty() },
            previewUrl = entity.previewUrlList,
            trackCount = entity.trackCount
        )
    }

    fun mapToEntity(playlist: NewPlaylist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.id,
            playlistName = playlist.name,
            playlistDescription = playlist.description,
            playlistTrackList = playlist.trackList.joinToString(","),
            previewUrlList = playlist.previewUrl,
            trackCount = playlist.trackCount
        )
    }
}