package com.example.myplaylist.settings.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri

class ExternalNavigatorImpl(
    private val context: Context,
    private val urlToShare: String,
    private val urlOffer: String,
    private val messageSubject: String,
    private val message: String,
    private val mailto: String,
    private val mail: String
) : ExternalNavigator {


    override fun shareLink() {
        val sendText = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, urlToShare)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(sendText, "Share").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun openEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(mailto)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            putExtra(Intent.EXTRA_SUBJECT, messageSubject)
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
            context.startActivity(intent)
    }
    override fun openLink() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlOffer)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

}