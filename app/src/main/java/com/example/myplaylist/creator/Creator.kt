package com.example.myplaylist.creator

import android.content.Context
import com.example.myplaylist.player.data.MediaPlayerWrapperImpl
import com.example.myplaylist.player.domain.use_case.MediaPlayerWrapper
import com.example.myplaylist.settings.data.ExternalNavigator
import com.example.myplaylist.settings.data.ExternalNavigatorImpl
import com.example.myplaylist.sharing.domain.SharingInteractor
import com.example.myplaylist.sharing.domain.SharingInteractorImpl

object Creator {


    fun getExternalNavigatorImpl(
        context: Context,
        urlToShare: String,
        urlOffer: String,
        messageSubject: String,
        message: String,
        mailto: String,
        mail: String
    ): ExternalNavigator {
        return ExternalNavigatorImpl(
            context,
            urlToShare,
            urlOffer,
            messageSubject,
            message,
            mailto,
            mail
        )
    }

    fun getSharingInteractorImpl(externalNavigator: ExternalNavigator): SharingInteractor {
        return SharingInteractorImpl(externalNavigator)
    }

}