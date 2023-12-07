package com.example.myplaylist

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate


const val MY_PREFERENCES = "PREFERENCES"
class SettingsActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchTheme: Switch
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val actionBack = findViewById<ImageButton>(R.id.buttonArow)
        actionBack.setOnClickListener {
            finish()
        }
        handSwitchTheme()
        handleShareButton()
        handMailButton()
        handAgreement()
    }

    private fun handSwitchTheme() {
        switchTheme = findViewById(R.id.switchTheme)
        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        switchTheme.isChecked = sharedPreferences.getBoolean(MY_PREFERENCES, false)

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(MY_PREFERENCES, isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            recreate()
        }
    }
    companion object {
        fun savedTheme(context: Context) {
            val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            val savedTheme = sharedPreferences.getBoolean(MY_PREFERENCES, false)

            if (savedTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
    private fun handleShareButton() {
        val shareButton = findViewById<ImageButton>(R.id.buttonShare)
        shareButton.setOnClickListener {
            val urlToShare = getString(R.string.url_share)
            val sendText = Intent(Intent.ACTION_SEND)
            sendText.type = urlToShare
            startActivity(sendText)
        }
    }

    private fun handMailButton() {
        val mailButton = findViewById<ImageButton>(R.id.buttonSupport)
        mailButton.setOnClickListener {
            val messageSubject = getString(R.string.message_subject)
            val message = getString(R.string.message)
            val mailto = getString(R.string.mail_to)
            val mail = getString(R.string.mail)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse(mailto)
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, messageSubject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }
    }

    private fun handAgreement() {
        val agreement = findViewById<ImageButton>(R.id.buttonAgreement)
        agreement.setOnClickListener {
            val urlToShare = getString(R.string.url_offer)
            val sendAgreement = Intent(Intent.ACTION_VIEW)
            sendAgreement.data = Uri.parse(urlToShare)
            startActivity(sendAgreement)
        }
    }
}