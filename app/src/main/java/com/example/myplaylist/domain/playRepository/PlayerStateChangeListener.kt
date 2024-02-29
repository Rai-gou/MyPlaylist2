package com.example.myplaylist.domain.playRepository

interface PlayerStateChangeListener {
    fun onStateChanged(isPlaying: Boolean)
}