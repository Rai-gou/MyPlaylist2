package com.example.myplaylist.player.domain

interface PlayerInteractor {
    fun addStateChangeListener(listener: PlayerStateChangeListener)
    fun removeStateChangeListener(listener: PlayerStateChangeListener)
}