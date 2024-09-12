package com.example.myplaylist.player.data.converters

import com.example.myplaylist.library.db.TrackInPlaylistEntity
import com.example.myplaylist.player.data.db.TrackEntity
import com.example.myplaylist.player.model.Track

class TrackInPlaylistConvertor {
    fun map(trackDto: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.previewUrl,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.addedTimestamp,
            trackDto.addedTimePlaylist
        )
    }

    fun map(trackDto: TrackInPlaylistEntity): Track {
        return Track(
            trackDto.trackId,
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.previewUrl,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.addedTimestamp,
            trackDto.addedTimePlaylist
        )
    }
}