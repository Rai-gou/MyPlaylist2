package com.example.myplaylist.domain.use_case

import android.media.MediaPlayer
import android.util.Log
import com.example.myplaylist.domain.use_case.MediaPlayerUseCase

class MediaPlayerUseCaseImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerUseCase {
    private var dataSourceUrl: String? = null
    private var isPrepared = false
    private var currentPosition: Int = 0

    override fun setDataSource(url: String) {
        dataSourceUrl = url
        Log.d("MyLog", "dataSourceUrl1: $url")
        mediaPlayer.reset()
        try {
            if (mediaPlayer.isPlaying || mediaPlayer.isLooping) {
                mediaPlayer.reset()
            }
            mediaPlayer.setDataSource(url)
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
}