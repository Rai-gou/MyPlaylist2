package com.example.myplaylist.player.data

import android.media.MediaPlayer
import android.util.Log
import com.example.myplaylist.player.domain.use_case.MediaPlayerWrapper

class MediaPlayerWrapperImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerWrapper {

    private var isPrepared = false
    private var currentPosition: Int = 0
    private var isPaused = false
    private var isPlaying = false

    override fun setDataSource(url: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            isPrepared = false

        } catch (e: Exception) {
            Log.e("MyLog", "Error setDataSource", e)
        }
    }

    override fun start() {
        if (isPrepared) {
            if (isPaused) {
                mediaPlayer.seekTo(currentPosition)
            }
            mediaPlayer.start()
            isPaused = false
            isPlaying = true
        } else {
            mediaPlayer.stop()
        }
    }

    override fun prepareAsync(onPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener {
            isPrepared = true
            onPrepared()
        }
        mediaPlayer.prepareAsync()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            seekToStart()
            listener()
        }
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPaused = true
            isPlaying = false
            currentPosition = mediaPlayer.currentPosition
        }
    }

    override fun stop() {
        mediaPlayer.stop()
        isPrepared = true
        currentPosition = 0
        isPaused = false
        isPlaying = false
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun seekToStart() {
        currentPosition = 0
        if (isPrepared)
            mediaPlayer.seekTo(0)
    }

    override fun isPaused(): Boolean {
        return isPaused
    }

    override fun seekTo(position: Int) {
        val duration = mediaPlayer.duration
        if (position <= duration) {
            mediaPlayer.seekTo(position)
        } else {
            Log.e("MyLog", "Invalid seek position: $position. Duration: $duration")
        }
    }
}