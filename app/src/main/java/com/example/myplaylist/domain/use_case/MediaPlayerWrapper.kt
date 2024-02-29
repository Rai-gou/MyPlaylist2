package com.example.myplaylist.domain.use_case

interface MediaPlayerWrapper {
    fun setDataSource(url: String)
    fun start()
    fun prepareAsync(onPrepared: () -> Unit)
    fun setOnCompletionListener(listener: () -> Unit)
    fun pause()
    fun stop()
    fun release()
    fun isPlaying(): Boolean
    fun currentPosition(): Int
    fun seekToStart()
}