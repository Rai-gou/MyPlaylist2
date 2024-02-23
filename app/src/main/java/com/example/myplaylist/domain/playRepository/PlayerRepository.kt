package com.example.myplaylist.domain.playRepository

interface PlayerRepository {
    fun isPlaying(): Boolean
    fun updatePlayerTime(currentPosition: Int)
    fun addStateChangeListener(listener: PlayerStateChangeListener)
    fun removeStateChangeListener(listener: PlayerStateChangeListener)
}