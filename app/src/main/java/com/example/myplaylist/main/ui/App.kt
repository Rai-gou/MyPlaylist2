package com.example.myplaylist.main.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.myplaylist.player.data.TrackRepository
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.RemoteTrackDataSourceImpl
import com.example.myplaylist.search.data.SearchRepository
import com.example.myplaylist.search.data.TrackDataSource
import com.example.myplaylist.search.domain.HistoryRepositoryImpl
import com.example.myplaylist.search.data.ItunesApi
import com.example.myplaylist.search.domain.SearchInteractor
import com.example.myplaylist.search.domain.SearchInteractorImpl
import com.example.myplaylist.search.domain.SearchRepositoryImpl
import com.example.myplaylist.search.domain.TrackDataSourceImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    lateinit var searchInteractor: SearchInteractor
        private set

    lateinit var historyRepository: HistoryRepository
        private set

    lateinit var searchRepository: SearchRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val itunesApi = provideItunesApi()
        val trackDataSource = TrackDataSourceImpl(RemoteTrackDataSourceImpl(itunesApi))
        val sharedPreferences =
            applicationContext.getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)

        historyRepository = provideHistoryRepository(sharedPreferences)
        val trackRepository = provideTrackRepository(trackDataSource)
        searchRepository = provideSearchRepository(trackRepository)

        searchInteractor =
            provideSearchInteractor(searchRepository, historyRepository, trackRepository)
    }

    fun provideItunesApi(): ItunesApi {
        val itunesBaseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(itunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ItunesApi::class.java)
    }

    fun provideSearchRepository(
        trackRepository: TrackRepository
    ): SearchRepository {
        return SearchRepositoryImpl(trackRepository)
    }

    fun provideSearchInteractor(
        searchRepository: SearchRepository,
        historyRepository: HistoryRepository,
        trackRepository: TrackRepository
    ): SearchInteractor {
        return SearchInteractorImpl(searchRepository, historyRepository, trackRepository)
    }

    fun provideHistoryRepository(sharedPreferences: SharedPreferences): HistoryRepository {
        return HistoryRepositoryImpl(sharedPreferences)
    }

    fun provideTrackRepository(
        trackDataSource: TrackDataSource
    ): TrackRepository {
        return TrackRepository(trackDataSource)
    }

}