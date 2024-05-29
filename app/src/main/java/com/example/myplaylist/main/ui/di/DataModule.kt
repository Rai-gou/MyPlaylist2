package com.example.myplaylist.main.ui.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.myplaylist.R
import com.example.myplaylist.player.data.MediaPlayerWrapperImpl
import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.player.domain.PlayerInteractor
import com.example.myplaylist.player.domain.PlayerInteractorImpl
import com.example.myplaylist.player.domain.use_case.MediaPlayerUseCase
import com.example.myplaylist.player.domain.use_case.MediaPlayerUseCaseImpl
import com.example.myplaylist.player.domain.use_case.MediaPlayerWrapper
import com.example.myplaylist.player.domain.use_case.TimerUseCase
import com.example.myplaylist.player.domain.use_case.TimerUseCaseImpl
import com.example.myplaylist.player.ui.MediaPlayViewModel
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.ItunesApi
import com.example.myplaylist.search.data.NetworkUtils
import com.example.myplaylist.search.data.RemoteTrackDataSourceImpl
import com.example.myplaylist.search.data.SearchRepository
import com.example.myplaylist.search.data.TrackDataSource
import com.example.myplaylist.search.domain.HistoryRepositoryImpl
import com.example.myplaylist.search.domain.SHARED_KEY_TRACK
import com.example.myplaylist.search.domain.SearchInteractor
import com.example.myplaylist.search.domain.SearchInteractorImpl
import com.example.myplaylist.search.domain.SearchRepositoryImpl
import com.example.myplaylist.search.domain.TrackDataSourceImpl
import com.example.myplaylist.search.ui.SearchViewModel
import com.example.myplaylist.settings.data.ExternalNavigator
import com.example.myplaylist.settings.data.ExternalNavigatorImpl
import com.example.myplaylist.settings.domain.AppSettings
import com.example.myplaylist.settings.domain.SettingsInteractor
import com.example.myplaylist.settings.domain.SettingsInteractorImpl
import com.example.myplaylist.settings.ui.MY_PREFERENCES
import com.example.myplaylist.settings.ui.SettingsViewModel
import com.example.myplaylist.sharing.domain.SharingInteractor
import com.example.myplaylist.sharing.domain.SharingInteractorImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single<AppSettings> { AppSettings(get()) }

    single { Gson() }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_KEY_TRACK, Context.MODE_PRIVATE)
    }

    single<RemoteTrackDataSourceImpl> { RemoteTrackDataSourceImpl(get()) } //RemoteTrackDataSourceImpl in SearchActivity
    single<TrackDataSource> { TrackDataSourceImpl(get()) } //trackDataSource in SearchActivity
    single<TrackRepository> { TrackRepository(get()) } //trackRepository in SearchActivity
    single<SearchRepository> { SearchRepositoryImpl(get()) }

    single { NetworkUtils(androidContext()) } //NetworkUtils



    single<HistoryRepository> {
        HistoryRepositoryImpl(
            androidContext().getSharedPreferences(
                SHARED_KEY_TRACK,
                Context.MODE_PRIVATE
            ),
            get()
        )
    }

    single<SearchInteractor> { SearchInteractorImpl(get(), get(), get()) }

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    single { MediaPlayer() }

    single<MediaPlayerWrapper> { MediaPlayerWrapperImpl(get()) }

    single<TimerUseCase> { TimerUseCaseImpl() }

    single<PlayerInteractor> { PlayerInteractorImpl() }

    single<MediaPlayerUseCase> { MediaPlayerUseCaseImpl(get()) }

    viewModel {
        MediaPlayViewModel(get(), get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(
            androidContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE)
        )
    }

    single(named("urlToShare")) { androidContext().getString(R.string.url_share) }
    single(named("urlOffer")) { androidContext().getString(R.string.url_offer) }
    single(named("messageSubject")) { androidContext().getString(R.string.message_subject) }
    single(named("message")) { androidContext().getString(R.string.message) }
    single(named("mailto")) { androidContext().getString(R.string.mail_to) }
    single(named("mail")) { androidContext().getString(R.string.mail) }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(
            androidContext(),
            get(named("urlToShare")),
            get(named("urlOffer")),
            get(named("messageSubject")),
            get(named("message")),
            get(named("mailto")),
            get(named("mail"))
        )
    }

    single<SharingInteractor> { SharingInteractorImpl(get()) }

    viewModel {
        SettingsViewModel(get(), get(), get())
    }
}