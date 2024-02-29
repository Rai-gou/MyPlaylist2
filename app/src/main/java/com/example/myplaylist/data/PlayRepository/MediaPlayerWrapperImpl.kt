package com.example.myplaylist.data.PlayRepository

import android.media.MediaPlayer
import android.util.Log
import com.example.myplaylist.domain.use_case.MediaPlayerWrapper

class MediaPlayerWrapperImpl : MediaPlayerWrapper {

    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var isPrepared = false
    private var currentPosition: Int = 0

    override fun setDataSource(url: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            Log.d("MyLog", "MediaPlayerWrapperImpl setDataSource: $url")
        } catch (e: Exception) {
            Log.e("MyLog", "Error", e)
        }
    }

    override fun start() {
        if (isPrepared) {
            mediaPlayer.seekTo(currentPosition)
            mediaPlayer.start()
        } else {
            Log.e("MyLog", "error.")
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
            listener()
        }
    }

    override fun pause() {
        if (mediaPlayer.isPlaying) {
            currentPosition = mediaPlayer.currentPosition
            mediaPlayer.pause()
        }
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun seekToStart() {
        currentPosition = 0
    }

}