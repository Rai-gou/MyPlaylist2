package com.example.myplaylist.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myplaylist.databinding.ActivitySettingsBinding
import org.koin.android.ext.android.inject

const val MY_PREFERENCES = "PREFERENCES"

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        initializeListeners()

    }

    private fun initializeViews() {
        viewModel.loadThemePreference()
        viewModel.themeSettings.observe(this) { themeSettings ->
            binding.switchTheme.isChecked = themeSettings.isNightModeEnabled
        }
    }

    private fun initializeListeners() {
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemePreference(isChecked)
        }

        binding.buttonArow.setOnClickListener {
            finish()
        }

        binding.buttonShare.setOnClickListener {
            viewModel.shareApp()
        }

        binding.buttonSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.buttonAgreement.setOnClickListener {
            viewModel.openTerms()
        }
    }
}