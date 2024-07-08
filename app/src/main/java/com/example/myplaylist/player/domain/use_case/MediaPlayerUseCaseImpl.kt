package com.example.myplaylist.player.domain.use_case

import android.util.Log

class MediaPlayerUseCaseImpl(private val mediaPlayerWrapper: MediaPlayerWrapper) : MediaPlayerUseCase {

    private var dataSourceUrl: String? = null
    private var pauseTime: Int = 0
    private var currentPosition: Int = 0
    override fun setDataSource(url: String) {
        dataSourceUrl = url
        Log.d("MyLog", "MediaPlayerWrapperImpl dataSourceUrl: $url")
        mediaPlayerWrapper.setDataSource(url)
    }

    override fun start() {
        mediaPlayerWrapper.start()
    }
    override fun resume() {
        mediaPlayerWrapper.start()
    }

    override fun prepareAsync(onPrepared: () -> Unit) {
        Log.d("MyLog", "prepareAsync four repiad")
        mediaPlayerWrapper.prepareAsync(onPrepared)
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayerWrapper.setOnCompletionListener{
            seekToStart()
            currentPosition = 0
            listener()
        }
    }

    override fun pause() {
        pauseTime = mediaPlayerWrapper.currentPosition()
        mediaPlayerWrapper.pause()
        mediaPlayerWrapper.seekTo(pauseTime)
    }

    override fun stop() {
        mediaPlayerWrapper.stop()
    }

    override fun release() {
        mediaPlayerWrapper.release()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayerWrapper.isPlaying()
    }

    override fun currentPosition(): Int {
        return mediaPlayerWrapper.currentPosition()
    }

    override fun isPaused(): Boolean {
        return mediaPlayerWrapper.isPaused()
    }
    override fun seekToStart() {
        return mediaPlayerWrapper.seekToStart()
    }

    override fun seekTo(pauseTime: Int) {
        this.pauseTime = pauseTime
        Log.d("MyLog", "seekTo pause1: ${this.pauseTime}")
    }
}