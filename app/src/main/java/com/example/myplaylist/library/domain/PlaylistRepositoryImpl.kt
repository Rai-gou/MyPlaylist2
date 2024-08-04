package com.example.myplaylist.library.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myplaylist.library.data.PlaylistRepository
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.player.data.db.AppDatabase
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context
) : PlaylistRepository {

    override suspend fun createPlaylist(name: String, imageUri: Uri?) {
        val playlistId = generatePlaylistId()
        val imagePath: String? = saveImageToPrivateStorage(context, imageUri)

        val playlist = PlaylistEntity(
            playlistId = playlistId,
            playlistName = name,
            playlistTrackList = "",
            previewUrlList = imagePath ?: "",
            trackCount = 0
        )
        Log.d("MyLog", "createPlaylist $playlist")
        appDatabase.playlistDao().insertPlaylist(playlist)
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

    override fun getAllPlaylists(): LiveData<List<PlaylistEntity>> {
        return appDatabase.playlistDao().getAllPlaylists()
    }

    override suspend fun getAllPlaylistsMediaPlay(): List<PlaylistEntity> {
        return appDatabase.playlistDao().getAllPlaylistsMediaPlay()
    }

    override suspend fun addTrackToPlaylistTrackList(playlistId: String, trackId: String): Boolean {
        val playlist = appDatabase.playlistDao().getPlaylistSync(playlistId)
        if (playlist != null) {
            val trackList = if (playlist.playlistTrackList.isEmpty()) {
                emptyList()
            } else {
                playlist.playlistTrackList.split(",")
            }

            return if (!trackList.contains(trackId)) {
                val updatedTrackList = if (trackList.isEmpty()) {
                    trackId
                } else {
                    "${playlist.playlistTrackList},$trackId"
                }
                appDatabase.playlistDao().addTrackToPlaylistTrackList(playlistId, updatedTrackList)
                appDatabase.playlistDao().incrementTrackCount(playlistId)
                true // Track was added
            } else {
                false // Track already exists
            }
        } else {
            Log.e("PlaylistRepositoryImpl", "Playlist not found.")
            return false
        }
    }

    override suspend fun incrementTrackCount(playlistId: String) {
        appDatabase.playlistDao().incrementTrackCount(playlistId)
    }

    override suspend fun insertPlaylist(playlist: PlaylistEntity) {
        appDatabase.playlistDao().insertPlaylist(playlist)
    }

    override fun getPlaylist(playlistId: String): LiveData<PlaylistEntity?> {
        return appDatabase.playlistDao().getPlaylist(playlistId)
    }
    override suspend fun getPlaylistNameById(playlistId: String): String {
        return appDatabase.playlistDao().getPlaylistName(playlistId)
    }

    override fun generatePlaylistId(): String {
        return UUID.randomUUID().toString()
    }
}