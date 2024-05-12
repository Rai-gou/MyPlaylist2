package com.example.myplaylist.settings.domain

import android.content.SharedPreferences

class SettingsInteractorImpl(private val sharedPreferences: SharedPreferences) : SettingsInteractor {

    private val THEME_KEY = "theme_key"

    override fun updateThemeSetting(themeSettings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(THEME_KEY, themeSettings.isNightModeEnabled).apply()
    }

    override fun getThemeSettings(): ThemeSettings {
        val isNightModeEnabled = sharedPreferences.getBoolean(THEME_KEY, false)
        return ThemeSettings(isNightModeEnabled)
    }
}