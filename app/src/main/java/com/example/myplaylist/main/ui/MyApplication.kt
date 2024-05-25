package com.example.myplaylist.main.ui

import android.app.Application
import com.example.myplaylist.main.ui.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(dataModule)
        }
    }
}