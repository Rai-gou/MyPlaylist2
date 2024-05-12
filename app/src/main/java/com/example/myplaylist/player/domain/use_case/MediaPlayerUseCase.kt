package com.example.myplaylist.player.domain.use_case

interface MediaPlayerUseCase {
    fun setDataSource(url: String)
    fun prepareAsync(onPrepared: () -> Unit)
    fun setOnCompletionListener(listener: () -> Unit)
    fun start()
    fun pause()
    fun stop()
    fun release()
    fun isPlaying(): Boolean
    fun currentPosition(): Int
    fun isPaused(): Boolean
    fun resume()
    fun seekToStart()
    fun seekTo(pauseTime: Int)
}