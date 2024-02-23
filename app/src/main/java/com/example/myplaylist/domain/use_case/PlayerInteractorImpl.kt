package com.example.myplaylist.domain.use_case

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.myplaylist.domain.timeRpository.TimerUpdate

class PlayerInteractorImpl : PlayerInteractor {
    private val handler = Handler(Looper.getMainLooper())
    private var timeUpdateListener: TimerUpdate? = null
    private val mediaPlayer = MediaPlayer()

    override fun setUpdateListener(listener: TimerUpdate) {
        timeUpdateListener = listener
    }

    override fun startUpdatingTime() {
        val updateTimeRunnable: Runnable = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    timeUpdateListener?.updateTime(mediaPlayer.currentPosition)
                    handler.postDelayed(this, 500)
                }
            }
        }
        handler.post(updateTimeRunnable)
    }

    override fun stopUpdatingTime() {
        handler.removeCallbacksAndMessages(null)
    }
}