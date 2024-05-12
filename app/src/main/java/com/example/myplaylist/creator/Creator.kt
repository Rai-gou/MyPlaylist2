package com.example.myplaylist.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.myplaylist.player.data.MediaPlayerWrapperImpl
import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.domain.PlayerInteractor
import com.example.myplaylist.player.domain.PlayerInteractorImpl
import com.example.myplaylist.player.domain.use_case.MediaPlayerWrapper
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.NetworkUtils
import com.example.myplaylist.search.data.RemoteTrackDataSourceImpl
import com.example.myplaylist.search.data.TrackDataSource
import com.example.myplaylist.search.domain.HistoryRepositoryImpl
import com.example.myplaylist.search.data.ItunesApi
import com.example.myplaylist.settings.data.ExternalNavigator
import com.example.myplaylist.settings.data.ExternalNavigatorImpl
import com.example.myplaylist.sharing.domain.SharingInteractor
import com.example.myplaylist.sharing.domain.SharingInteractorImpl

object Creator {

    fun getMediaPlayerWrapperImpl(): MediaPlayerWrapper {
        return MediaPlayerWrapperImpl()
    }

    fun getTrackRepository(trackDataSource: TrackDataSource): TrackRepository {
        return TrackRepository(trackDataSource)
    }

    fun getNetworkUtils(context: Context): NetworkUtils {
        return NetworkUtils(context)
    }

    fun getRemoteTrackDataSourceImpl(itunesApi: ItunesApi): RemoteTrackDataSourceImpl {
        return RemoteTrackDataSourceImpl(itunesApi)
    }

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