package com.example.myplaylist.main.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.myplaylist.databinding.ActivityMainBinding
import com.example.myplaylist.main.ui.di.dataModule
import com.example.myplaylist.search.ui.SearchActivity
import com.example.myplaylist.settings.domain.AppSettings
import com.example.myplaylist.settings.ui.SettingsActivity
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val appSettings: AppSettings by inject()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedThemeMode = appSettings.themeMode
        updateTheme(savedThemeMode == AppCompatDelegate.MODE_NIGHT_YES)

        binding.mediaButton.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        binding.searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateTheme(isNightModeEnabled: Boolean) {
        if (isNightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}