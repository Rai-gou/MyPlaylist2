package com.example.myplaylist.data.timeRepository

import android.os.Handler
import android.os.Looper
import com.example.myplaylist.domain.timeRpository.TimerState
import com.example.myplaylist.domain.timeRpository.TimerUpdate
import com.example.myplaylist.domain.timeRpository.TimerRepository

class TimerRepositoryImpl : TimerRepository, TimerUpdate {
    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition = 0
    private var isTimerRunning = false

    override fun startTimer() {
        if (!isTimerRunning) {
            handler.post(updateTime)
            isTimerRunning = true
        }
    }

    override fun stopTimer() {
        handler.removeCallbacks(updateTime)
        isTimerRunning = false
    }

    private val updateTime: Runnable = object : Runnable {
        override fun run() {
            currentPosition += 500
            updateTime(currentPosition)
            handler.postDelayed(this, 500)
        }
    }

    override fun updateTime(currentPosition: Int) {
    }

    override fun startUpdatingTime() {
        startTimer()
    }

    override fun stopUpdatingTime() {
        stopTimer()
    }

    override fun updateTimerState(state: TimerState) {
    }

}