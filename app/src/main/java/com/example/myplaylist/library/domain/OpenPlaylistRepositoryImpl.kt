package com.example.myplaylist.library.domain

import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.OpenPlaylistRepository
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.data.converters.PlaylistDbConverter
import com.example.myplaylist.player.data.converters.TrackInPlaylistConvertor
import com.example.myplaylist.player.data.db.AppDatabase

class OpenPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackInPlaylistConverter: TrackInPlaylistConvertor
) : OpenPlaylistRepository {

    override suspend fun getPlaylistById(playlistId: String): NewPlaylist? {
        val playlistEntity = appDatabase.playlistDao().getPlaylistSync(playlistId)
        return playlistEntity?.let { playlistDbConverter.mapToDomain(it) }
    }

    override suspend fun getPlaylistWithTracks(playlistId: String): NewPlaylistWithTracks? {
        val playlistEntity = appDatabase.playlistDao().getPlaylistSync(playlistId)
        playlistEntity?.let { playlist ->
            val trackIds = playlist.playlistTrackList.split(",").filter { it.isNotEmpty() }

            val trackEntities = appDatabase.trackInPlaylistDao().getTracksInPlaylist(trackIds)
            val tracks = trackEntities.map { trackInPlaylistConverter.map(it) }

            return NewPlaylistWithTracks(
                id = playlist.playlistId,
                name = playlist.playlistName,
                description = playlist.playlistDescription,
                trackList = tracks,
                previewUrl = playlist.previewUrlList,
                trackCount = tracks.size
            )
        }
        return null
    }
    override suspend fun removeTrackFromPlaylist(trackId: String, playlistId: String) {
        val playlist = appDatabase.playlistDao().getPlaylistSync(playlistId)
        playlist?.let {
            val updatedTrackList = it.playlistTrackList.split(",").filter { id -> id != trackId }.joinToString(",")
            appDatabase.playlistDao().updatePlaylistTrackList(playlistId, updatedTrackList)

            val playlistsContainingTrack = appDatabase.playlistDao().getPlaylistsContainingTrack(trackId)
            if (playlistsContainingTrack.isEmpty()) {
                appDatabase.trackInPlaylistDao().deleteTrackIfNotInAnyPlaylist(trackId)
            }
        }
    }
    override suspend fun deletePlaylist(playlistId: String) {
        appDatabase.playlistDao().deletePlaylist(playlistId)
    }
}