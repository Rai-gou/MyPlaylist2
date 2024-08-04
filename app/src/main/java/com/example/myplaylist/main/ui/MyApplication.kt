package com.example.myplaylist.main.ui

import android.app.Application
import com.example.myplaylist.main.di.dataModule
import com.example.myplaylist.player.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val database: AppDatabase by lazy {
            AppDatabase.getDatabase(this)
        }
        startKoin {
            androidContext(this@MyApplication)
            modules(dataModule)
        }
    }
}