package com.example.myplaylist.player.domain.use_case

import com.example.myplaylist.player.domain.PlayerStateChangeListener
import java.util.Timer
import java.util.TimerTask

const val CURRENT_TIME_MILLIS = 500

class TimerUseCaseImpl: TimerUseCase {
    private var timer: Timer? = null
    private var currentPositionTime: Int = 0
    private var timerUpdateListener: PlayerStateChangeListener? = null
    private var isTimerRunning: Boolean = false

    override fun startTimer() {
        if (!isTimerRunning) {
            timer = Timer()
            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    currentPositionTime += CURRENT_TIME_MILLIS
                    timerUpdateListener?.onTimeUpdate(currentPositionTime)
                }
            }, 0, CURRENT_TIME_MILLIS.toLong())
            isTimerRunning = true
        }
    }

    override fun stopTimer() {
        timer?.cancel()
        isTimerRunning = false
    }

    override fun resetTimer() {
        stopTimer()
        currentPositionTime = 0
    }

    override fun startUpdatingTime() {
        startTimer()
    }

    override fun stopUpdatingTime() {
        stopTimer()
    }

    fun setTimerUpdateListener(listener: PlayerStateChangeListener) {
        this.timerUpdateListener = listener
    }
}