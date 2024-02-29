package com.example.myplaylist.domain.playRepository

interface PlayerRepository {
    fun isPlaying(): Boolean
    fun addStateChangeListener(listener: PlayerStateChangeListener)
    fun removeStateChangeListener(listener: PlayerStateChangeListener)
}