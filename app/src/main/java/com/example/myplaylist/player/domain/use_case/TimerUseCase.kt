package com.example.myplaylist.player.domain.use_case

import com.example.myplaylist.player.domain.PlayerStateChangeListener
import kotlinx.coroutines.CoroutineScope

interface TimerUseCase {
    fun startTimer(scope: CoroutineScope)
    fun stopTimer()
    fun resetTimer()
    fun startUpdatingTime(scope: CoroutineScope)
    fun stopUpdatingTime()
    fun setTimerUpdateListener(listener: PlayerStateChangeListener)
}