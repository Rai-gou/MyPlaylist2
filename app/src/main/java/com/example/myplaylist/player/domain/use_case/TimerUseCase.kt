package com.example.myplaylist.player.domain.use_case

import com.example.myplaylist.player.domain.PlayerStateChangeListener

interface TimerUseCase {
    fun startTimer()
    fun stopTimer()
    fun resetTimer()
    fun startUpdatingTime()
    fun stopUpdatingTime()
    fun setTimerUpdateListener(listener: PlayerStateChangeListener)
}