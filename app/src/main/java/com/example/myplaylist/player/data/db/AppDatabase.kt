package com.example.myplaylist.player.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myplaylist.library.db.PlaylistEntity
import com.example.myplaylist.library.db.TrackInPlaylistEntity
import com.example.myplaylist.player.data.db.dao.PlaylistDao
import com.example.myplaylist.player.data.db.dao.TrackDao
import com.example.myplaylist.player.data.db.dao.TrackInPlaylistDao

@Database(entities = [PlaylistEntity::class, TrackEntity::class, TrackInPlaylistEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_2_3) // Убедитесь, что миграции применяются
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Миграция для создания новой таблицы
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Логирование миграции
                Log.d("DatabaseMigration", "Applying migration 2 to 3")
                database.execSQL(
                    """
            CREATE TABLE track_in_playlist (
                trackId TEXT PRIMARY KEY NOT NULL,
                trackName TEXT,
                artistName TEXT,
                trackTimeMillis INTEGER,
                artworkUrl100 TEXT,
                previewUrl TEXT,
                collectionName TEXT,
                releaseDate TEXT,
                primaryGenreName TEXT,
                country TEXT,
                addedTimestamp INTEGER
            )
            """
                )
            }
        }
    }
}