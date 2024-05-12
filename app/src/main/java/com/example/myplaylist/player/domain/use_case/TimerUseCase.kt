package com.example.myplaylist.player.domain.use_case

interface TimerUseCase {
    fun startTimer()
    fun stopTimer()
    fun resetTimer()
    fun startUpdatingTime()
    fun stopUpdatingTime()
}