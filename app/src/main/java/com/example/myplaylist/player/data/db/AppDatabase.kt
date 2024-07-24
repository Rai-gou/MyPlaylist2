package com.example.myplaylist.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myplaylist.player.data.db.dao.TrackDao

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}