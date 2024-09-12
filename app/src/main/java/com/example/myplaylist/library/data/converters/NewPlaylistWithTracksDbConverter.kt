package com.example.myplaylist.library.data.converters

import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.player.model.Track

class NewPlaylistWithTracksDbConverter {

    fun mapToDomain(entity: PlaylistEntity): NewPlaylistWithTracks {
        return NewPlaylistWithTracks(
            id = entity.playlistId,
            name = entity.playlistName,
            description = entity.playlistDescription,
            trackList = entity.playlistTrackList
                .split(",")
                .filter { it.isNotEmpty() }
                .map { trackString -> convertToTrack(trackString) }, // Convert String to Track
            previewUrl = entity.previewUrlList,
            trackCount = entity.trackCount
        )
    }

    fun mapToEntity(playlist: NewPlaylistWithTracks): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.id,
            playlistName = playlist.name,
            playlistDescription = playlist.description,
            playlistTrackList = playlist.trackList
                .map { track -> convertToString(track) }
                .joinToString(","),
            previewUrlList = playlist.previewUrl,
            trackCount = playlist.trackCount
        )
    }

    private fun convertToTrack(trackString: String): Track {
        return Track.fromString(trackString)
    }

    private fun convertToString(track: Track): String {
        return track.toString()
    }
}