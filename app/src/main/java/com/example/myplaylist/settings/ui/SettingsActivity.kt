package com.example.myplaylist.settings.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylist.R
import com.example.myplaylist.creator.Creator
import com.example.myplaylist.databinding.ActivitySettingsBinding
import com.example.myplaylist.settings.domain.AppSettings
import com.example.myplaylist.settings.domain.SettingsInteractorImpl
import org.koin.android.ext.android.inject

const val MY_PREFERENCES = "PREFERENCES"

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by inject()
    private val appSettings: AppSettings by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val settingsInteractor =
            SettingsInteractorImpl(getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE))*/

        val urlToShare = getString(R.string.url_share)
        val urlOffer = getString(R.string.url_offer)
        val messageSubject = getString(R.string.message_subject)
        val message = getString(R.string.message)
        val mailto = getString(R.string.mail_to)
        val mail = getString(R.string.mail)

        /*val externalNavigator = Creator.getExternalNavigatorImpl(
            this,
            urlToShare,
            urlOffer,
            messageSubject,
            message,
            mailto,
            mail
        )*/

        //val sharingInteractor = Creator.getSharingInteractorImpl(externalNavigator)

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