package com.example.myplaylist.domain.timeRpository

interface TimerUpdate {
    fun startUpdatingTime()
    fun stopUpdatingTime()
    fun updateTimerState(state: TimerState)
    fun updateTime(currentPosition: Int)
}