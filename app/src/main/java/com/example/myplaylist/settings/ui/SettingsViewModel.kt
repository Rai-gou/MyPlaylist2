package com.example.myplaylist.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myplaylist.settings.domain.AppSettings
import com.example.myplaylist.settings.domain.SettingsInteractor
import com.example.myplaylist.settings.domain.ThemeSettings
import com.example.myplaylist.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _themeSettings = MutableLiveData<ThemeSettings>()
    val themeSettings: LiveData<ThemeSettings> = _themeSettings

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun saveThemePreference(isChecked: Boolean) {
        val themeSettings = ThemeSettings(isChecked)
        settingsInteractor.updateThemeSetting(themeSettings)
        updateTheme(isChecked)
        appSettings.themeMode =
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
    }

    private fun updateTheme(isNightModeEnabled: Boolean) {
        if (isNightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun loadThemePreference() {
        val savedThemeMode = appSettings.themeMode
        updateTheme(savedThemeMode == AppCompatDelegate.MODE_NIGHT_YES)
        _themeSettings.value = ThemeSettings(savedThemeMode == AppCompatDelegate.MODE_NIGHT_YES)
    }
}