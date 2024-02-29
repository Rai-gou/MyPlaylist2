package com.example.myplaylist.creator

import com.example.myplaylist.data.PlayRepository.MediaPlayerWrapperImpl
import com.example.myplaylist.data.PlayRepository.PlayerListenerImpl
import com.example.myplaylist.data.PlayRepository.PlayerRepositoryImpl
import com.example.myplaylist.data.timeRepository.TimerRepositoryImpl
import com.example.myplaylist.domain.callback.MediaPlayCallback
import com.example.myplaylist.domain.use_case.MediaPlayerWrapper
import com.example.myplaylist.domain.playRepository.PlayerRepository
import com.example.myplaylist.domain.timeRpository.TimerRepository
import com.example.myplaylist.domain.use_case.PlayerInteractor
import com.example.myplaylist.domain.use_case.PlayerInteractorImpl
import com.example.myplaylist.domain.use_case.PlayerListener

object Creator {
    fun getPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    fun getMediaPlayerWrapper(): MediaPlayerWrapper {
        return MediaPlayerWrapperImpl()
    }

    fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun getPlayerListener(callback: MediaPlayCallback): PlayerListener {
        val playerRepository = getPlayerRepository()
        return PlayerListenerImpl(playerRepository, callback)
    }

    fun getTimerRepository(): TimerRepository {
        return TimerRepositoryImpl()
    }
}