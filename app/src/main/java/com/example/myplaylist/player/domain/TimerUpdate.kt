package com.example.myplaylist.player.domain

interface TimerUpdate {
    fun startUpdatingTime()
    fun stopUpdatingTime()
    fun updateTime(currentPosition: Int)
    fun resetTimer()
}