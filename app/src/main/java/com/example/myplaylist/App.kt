package com.example.myplaylist

import android.app.Application


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SettingsActivity.savedTheme(this)
    }

}