package com.example.myplaylist.domain.use_case

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
}