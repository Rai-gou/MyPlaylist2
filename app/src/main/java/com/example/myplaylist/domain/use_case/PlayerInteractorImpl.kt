package com.example.myplaylist.domain.use_case

import com.example.myplaylist.domain.timeRpository.TimerUpdate

class PlayerInteractorImpl : PlayerInteractor {
    private var timeUpdateListener: TimerUpdate? = null

    override fun setUpdateListener(listener: TimerUpdate) {
        timeUpdateListener = listener
    }

    override fun startUpdatingTime(currentPosition: Int) {
        timeUpdateListener?.updateTime(currentPosition)
    }

    override fun stopUpdatingTime() {
        timeUpdateListener = null
    }
}