package com.example.myplaylist.data.PlayRepository

import com.example.myplaylist.domain.playRepository.PlayerRepository
import com.example.myplaylist.domain.playRepository.PlayerStateChangeListener

class PlayerRepositoryImpl : PlayerRepository {
    private val stateChangeListeners = mutableListOf<PlayerStateChangeListener>()
    private var isPlayerPlaying = false

    override fun isPlaying(): Boolean {
        return isPlayerPlaying
    }

    override fun addStateChangeListener(listener: PlayerStateChangeListener) {
        stateChangeListeners.add(listener)
    }

    override fun removeStateChangeListener(listener: PlayerStateChangeListener) {
        stateChangeListeners.remove(listener)
    }
}