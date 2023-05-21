package com.example.myplaylist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionBack = findViewById<ImageButton>(R.id.imageButton)

        actionBack.setOnClickListener {

            finish()
        }

        val sharing = findViewById<ImageButton>(R.id.imageButton2)

        sharing.setOnClickListener {
            val urlToShare = getString(R.string.urlToShareProfile)
            val sendText = Intent(Intent.ACTION_SEND)
            sendText.type = urlToShare
            startActivity(sendText)
        }

        val shareButton = findViewById<ImageButton>(R.id.imageButton3)

        shareButton.setOnClickListener {
            val messageSubject = getString(R.string.messageSubject)
            val message = getString(R.string.message)
            val mail = getString(R.string.mail)

            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, messageSubject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        val agreement = findViewById<ImageButton>(R.id.imageButton5)

        agreement.setOnClickListener {
            val urlToShare = getString(R.string.urlToShareOffer)
            val sendAgreement = Intent(Intent.ACTION_VIEW)
            sendAgreement.data = Uri.parse(urlToShare)
            startActivity(sendAgreement)
        }

    }
}