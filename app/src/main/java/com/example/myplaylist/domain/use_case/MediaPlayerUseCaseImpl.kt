package com.example.myplaylist.domain.use_case

class MediaPlayerUseCaseImpl(private val mediaPlayerWrapper: MediaPlayerWrapper) :
    MediaPlayerUseCase {

    private var dataSourceUrl: String? = null
    override fun setDataSource(url: String) {
        dataSourceUrl = url
        mediaPlayerWrapper.setDataSource(url)
    }

    override fun start() {
        mediaPlayerWrapper.start()
    }

    override fun prepareAsync(onPrepared: () -> Unit) {
        mediaPlayerWrapper.prepareAsync(onPrepared)
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayerWrapper.setOnCompletionListener(listener)
    }

    override fun pause() {
        mediaPlayerWrapper.pause()
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
}