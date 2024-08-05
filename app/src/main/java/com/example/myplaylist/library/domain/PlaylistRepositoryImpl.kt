package com.example.myplaylist.library.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.PlaylistRepository
import com.example.myplaylist.library.data.converters.PlaylistDbConverter
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.db.TrackInPlaylistEntity
import com.example.myplaylist.player.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(name: String, imageUri: Uri?) {
        val playlistId = generatePlaylistId()
        val imagePath: String? = saveImageToPrivateStorage(context, imageUri)

        val playlistEntity = PlaylistEntity(
            playlistId = playlistId,
            playlistName = name,
            playlistTrackList = "",
            previewUrlList = imagePath ?: "",
            trackCount = 0
        )
        Log.d("MyLog", "createPlaylist $playlistEntity")
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    private fun saveImageToPrivateStorage(context: Context, uri: Uri?): String? {
        return try {
            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
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

    override fun getAllPlaylists(): Flow<List<NewPlaylist>> {
        return appDatabase.playlistDao().getAllPlaylists().map { entities ->
            entities.map { playlistDbConverter.mapToDomain(it) }
        }
    }

    override suspend fun getAllPlaylistsMediaPlay(): List<NewPlaylist> {
        return appDatabase.playlistDao().getAllPlaylistsMediaPlay().map { playlistDbConverter.mapToDomain(it) }
    }

    override suspend fun addTrackToPlaylistTrackList(playlistId: String, track: TrackInPlaylistEntity): Boolean {
        val playlistEntity = appDatabase.playlistDao().getPlaylistSync(playlistId)
        addTrack(track)

        if (playlistEntity != null) {
            val trackList = if (playlistEntity.playlistTrackList.isEmpty()) {
                emptyList()
            } else {
                playlistEntity.playlistTrackList.split(",")
            }

            return if (!trackList.contains(track.trackId)) { // Используйте track.trackId
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
            Log.e("PlaylistRepositoryImpl", "Playlist not found.")
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
}