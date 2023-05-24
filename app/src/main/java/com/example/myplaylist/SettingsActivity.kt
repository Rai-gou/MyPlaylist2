package com.example.myplaylist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionBack = findViewById<ImageButton>(R.id.buttonArow)

        actionBack.setOnClickListener {

            finish()
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