package com.example.myplaylist.domain.use_case

import com.example.myplaylist.domain.timeRpository.TimerRepository

class TimerUseCaseImpl(private val timerRepository: TimerRepository) : TimerUseCase {

    override fun startTimer() {
        timerRepository.startTimer()
    }

    override fun stopTimer() {
        timerRepository.stopTimer()
    }
}