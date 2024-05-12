package com.example.myplaylist.player.domain

class PlayerInteractorImpl : PlayerInteractor {
    private val stateChangeListeners = mutableListOf<PlayerStateChangeListener>()

    override fun addStateChangeListener(listener: PlayerStateChangeListener) {
        stateChangeListeners.add(listener)
    }

    override fun removeStateChangeListener(listener: PlayerStateChangeListener) {
        stateChangeListeners.remove(listener)
    }
}