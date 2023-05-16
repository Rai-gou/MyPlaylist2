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
            val urlToShare = "https://practicum.yandex.ru/profile/android-developer/"
            val sendText = Intent(Intent.ACTION_SEND)
            sendText.type = urlToShare
            startActivity(sendText)
        }

        val shareButton = findViewById<ImageButton>(R.id.imageButton3)

        shareButton.setOnClickListener {
            val messageSubject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"

            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("raigou@yandex.ru"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, messageSubject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        val agreement = findViewById<ImageButton>(R.id.imageButton5)

        agreement.setOnClickListener {
            val urlToShare = "https://yandex.ru/legal/practicum_offer/"
            val sendAgreement = Intent(Intent.ACTION_VIEW)
            sendAgreement.data = Uri.parse(urlToShare)
            startActivity(sendAgreement)
        }

    }
}