package com.example.myplaylist.settings.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class AppSettings(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME_MODE = "themeMode"
    }

    var themeMode: Int
        get() = sharedPreferences.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) = sharedPreferences.edit().putInt(KEY_THEME_MODE, value).apply()
}