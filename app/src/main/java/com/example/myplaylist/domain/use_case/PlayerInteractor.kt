package com.example.myplaylist.domain.use_case

import com.example.myplaylist.domain.timeRpository.TimerUpdate

interface PlayerInteractor {
    fun setUpdateListener(listener: TimerUpdate)
    fun startUpdatingTime(currentPosition: Int)
    fun stopUpdatingTime()
}