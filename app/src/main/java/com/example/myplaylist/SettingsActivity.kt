package com.example.myplaylist

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionBack = findViewById<ImageButton>(R.id.buttonArow)

        actionBack.setOnClickListener {

            finish()
        }

        val switchTheme = findViewById<Switch>(R.id.switchTheme)

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()
                }
            } else {
                if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                }
            }
        }


        val shareButton = findViewById<ImageButton>(R.id.buttonShare)

        shareButton.setOnClickListener {
            val urlToShare = getString(R.string.url_share)
            val sendText = Intent(Intent.ACTION_SEND)
            sendText.type = urlToShare
            startActivity(sendText)
        }

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

        val agreement = findViewById<ImageButton>(R.id.buttonAgreement)

        agreement.setOnClickListener {
            val urlToShare = getString(R.string.url_offer)
            val sendAgreement = Intent(Intent.ACTION_VIEW)
            sendAgreement.data = Uri.parse(urlToShare)
            startActivity(sendAgreement)
        }

    }
}