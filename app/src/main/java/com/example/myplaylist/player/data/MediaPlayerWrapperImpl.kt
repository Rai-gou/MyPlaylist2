package com.example.myplaylist.player.data

import android.media.MediaPlayer
import android.util.Log
import com.example.myplaylist.player.domain.use_case.MediaPlayerWrapper

class MediaPlayerWrapperImpl : MediaPlayerWrapper {

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var isPrepared = false
    private var currentPosition: Int = 0
    private var isPaused = false
    private var isPlaying = false

    override fun setDataSource(url: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            Log.d("MyLog", "MediaPlayerWrapperImpl setDataSource: $url")
            isPrepared = false

        } catch (e: Exception) {
            Log.e("MyLog", "Error setDataSource", e)
        }
    }

    override fun start() {
        if (isPrepared) {
            Log.d("MyLog", "start Position: $currentPosition")
            if (isPaused) {
                mediaPlayer.seekTo(currentPosition)
            }
            mediaPlayer.start()
            isPaused = false
            isPlaying = true
            Log.d("MyLog", "start: ${mediaPlayer.start()}")
        } else {
            mediaPlayer.stop()
            Log.e("MyLog", "error.")
        }
    }

    override fun prepareAsync(onPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener {
            isPrepared = true
            onPrepared()
            Log.d("MyLog", "preparation prepareAsync")

        }
        mediaPlayer.prepareAsync()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            Log.d("MyLog", "One")
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
        Log.d("MyLog", "Seek to: $currentPosition")
    }

    override fun isPaused(): Boolean {
        return isPaused
    }

    override fun seekTo(position: Int) {
        val duration = mediaPlayer.duration
        if (position <= duration) {
            Log.d("MyLog", "SeekTo podsition2: $position")
            mediaPlayer.seekTo(position)
        } else {
            Log.e("MyLog", "Invalid seek position: $position. Duration: $duration")
        }
    }
}