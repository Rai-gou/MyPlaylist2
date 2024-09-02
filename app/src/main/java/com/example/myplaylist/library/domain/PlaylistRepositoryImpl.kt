package com.example.myplaylist.library.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.library.data.PlaylistRepository
import com.example.myplaylist.library.data.converters.PlaylistDbConverter
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.db.TrackInPlaylistEntity
import com.example.myplaylist.player.data.converters.TrackInPlaylistConvertor
import com.example.myplaylist.player.data.db.AppDatabase
import com.example.myplaylist.player.model.Track
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackInPlaylistConverter: TrackInPlaylistConvertor
) : PlaylistRepository {

    override suspend fun createPlaylist(name: String, description: String, imageUri: Uri?) {
        val playlistId = generatePlaylistId()
        val imagePath: String? = saveImageToPrivateStorage(context, imageUri)

        val playlistEntity = PlaylistEntity(
            playlistId = playlistId,
            playlistName = name,
            playlistDescription = description.ifEmpty { "" },
            playlistTrackList = "",
            previewUrlList = imagePath ?: "",
            trackCount = 0
        )

        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    private fun saveImageToPrivateStorage(context: Context, uri: Uri?): String? {
        return try {
            val filePath =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val fileName = "cover_${System.currentTimeMillis()}.jpg"
            val file = File(filePath, fileName)
            if (uri != null) {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        BitmapFactory.decodeStream(inputStream)
                            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
                    }
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getAllPlaylistsMediaPlay(): List<NewPlaylist> {
        return appDatabase.playlistDao().getAllPlaylistsMediaPlay()
            .map { playlistDbConverter.mapToDomain(it) }
    }

    override suspend fun addTrackToPlaylistTrackList(playlistId: String, track: Track): Boolean {
        val trackInPlaylistEntity = trackInPlaylistConverter.map(track)
        val playlistEntity = appDatabase.playlistDao().getPlaylistSync(playlistId)
        addTrack(trackInPlaylistEntity)

        if (playlistEntity != null) {
            val trackList = if (playlistEntity.playlistTrackList.isEmpty()) {
                emptyList()
            } else {
                playlistEntity.playlistTrackList.split(",")
            }

            return if (!trackList.contains(track.trackId)) {
                val updatedTrackList = if (trackList.isEmpty()) {
                    track.trackId
                } else {
                    "${playlistEntity.playlistTrackList},${track.trackId}"
                }
                appDatabase.playlistDao().addTrackToPlaylistTrackList(playlistId, updatedTrackList)
                appDatabase.playlistDao().incrementTrackCount(playlistId)
                true
            } else {
                false
            }
        } else {
            return false
        }
    }

    suspend fun addTrack(track: TrackInPlaylistEntity) {
        appDatabase.trackInPlaylistDao().insertTrackInPlaylist(track)
    }

    override suspend fun incrementTrackCount(playlistId: String) {
        appDatabase.playlistDao().incrementTrackCount(playlistId)
    }

    override suspend fun insertPlaylist(playlist: NewPlaylist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConverter.mapToEntity(playlist))
    }

    override suspend fun getPlaylistNameById(playlistId: String): String {
        return appDatabase.playlistDao().getPlaylistName(playlistId)
    }

    override fun generatePlaylistId(): String {
        return UUID.randomUUID().toString()
    }

    override fun playlistDescription(): String {
        return toString()
    }

    override suspend fun getPlaylistById(playlistId: String): NewPlaylist? {
        val playlistEntity = appDatabase.playlistDao().getPlaylistSync(playlistId)
        return playlistEntity?.let { playlistDbConverter.mapToDomain(it) }
    }

    override suspend fun updatePlaylist(
        playlistId: String,
        name: String,
        description: String,
        imageUri: Uri?
    ) {
        try {
            val currentPlaylist = appDatabase.playlistDao().getPlaylistSync(playlistId)
            val imagePath: String? = if (imageUri != null) {
                saveImageToPrivateStorage(context, imageUri)
            } else {
                currentPlaylist?.previewUrlList
            }
            if (imagePath != null) {
                appDatabase.playlistDao().updatePlaylist(playlistId, name, description, imagePath)
            } else {
                Log.e("PlaylistRepositoryImpl", "Failed to update playlist: imagePath is null")
            }
        } catch (e: Exception) {
            Log.e("PlaylistRepositoryImpl", "Error updating playlist: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAllPlaylistsWithTracks(): List<NewPlaylistWithTracks> {
        val playlists = appDatabase.playlistDao().getAllPlaylistsSync()
        Log.d("PlaylistRepositoryImpl", "Playlists from DB: ${playlists.size}")

        return playlists.map { playlist ->
            val trackIds = playlist.playlistTrackList.split(",").filter { it.isNotEmpty() }
            val tracks = if (trackIds.isNotEmpty()) {
                appDatabase.trackInPlaylistDao().getTracksInPlaylist(trackIds).map { trackEntity ->
                    trackInPlaylistConverter.map(trackEntity)
                }
            } else {
                emptyList()
            }

            val updatedTrackCount = tracks.size

            NewPlaylistWithTracks(
                id = playlist.playlistId,
                name = playlist.playlistName,
                description = playlist.playlistDescription,
                previewUrl = playlist.previewUrlList,
                trackCount = updatedTrackCount,
                trackList = tracks
            )
        }
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
}