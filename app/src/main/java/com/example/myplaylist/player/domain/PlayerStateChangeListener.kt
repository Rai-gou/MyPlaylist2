package com.example.myplaylist.player.domain

interface PlayerStateChangeListener {
    fun onTimeUpdate(currentPosition: Int)
}