package com.example.myplaylist.player.domain

import kotlinx.coroutines.CoroutineScope

interface TimerUpdate {
    fun startUpdatingTime()
    fun stopUpdatingTime()
    fun resetTimer()
}