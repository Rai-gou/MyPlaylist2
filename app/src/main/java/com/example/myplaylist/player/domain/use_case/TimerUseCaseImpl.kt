package com.example.myplaylist.player.domain.use_case

import android.util.Log
import com.example.myplaylist.player.domain.PlayerStateChangeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

const val CURRENT_TIME_MILLIS = 500

class TimerUseCaseImpl : TimerUseCase {
    private var currentPositionTime: Int = 0
    private var timerJob: Job? = null
    private var isTimerRunning: Boolean = false
    private var timerUpdateListener: PlayerStateChangeListener? = null

    override fun startTimer() {
        if (!isTimerRunning) {
            timerJob = CoroutineScope(Dispatchers.Default).launch {
                timeFlow().collect {
                    currentPositionTime += CURRENT_TIME_MILLIS
                    Log.d("MyLog", "Timer updated: $currentPositionTime")
                    timerUpdateListener?.onTimeUpdate(currentPositionTime)
                }
            }
            isTimerRunning = true
        }
    }

    override fun stopTimer() {
        timerJob?.cancel()
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

    override fun setTimerUpdateListener(listener: PlayerStateChangeListener) {
        this.timerUpdateListener = listener
        Log.d("MyLog", "TimerUpdateListener set: $listener")
    }

    private fun timeFlow(): Flow<Int> = flow {
        while (true) {
            delay(CURRENT_TIME_MILLIS.toLong())
            emit(currentPositionTime + CURRENT_TIME_MILLIS)
        }
    }.flowOn(Dispatchers.Default)
}