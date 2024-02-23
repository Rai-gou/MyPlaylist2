package com.example.myplaylist.data.PlayRepository

import com.example.myplaylist.domain.use_case.PlayerListener
import com.example.myplaylist.domain.models.PlayerState
import com.example.myplaylist.ui.MediaPlay


class PlayerListenerImpl(
    private val playerRepository: PlayerRepositoryImpl,
    private val mediaPlay: MediaPlay

) : PlayerListener {
    private var isPlaying = playerRepository.isPlaying()

    override fun onChange(state: PlayerState) {
        when (state) {
            PlayerState.INIT -> {
                if (playerIsReady()) {
                    PlayerState.PLAYING
                } else {
                    state
                }
            }

            PlayerState.PLAYING -> {
                if (playerIsStopped()) {
                    PlayerState.PAUSE
                } else {
                    state
                }
            }

            PlayerState.PAUSE -> {
                if (playerIsResumed()) {
                    PlayerState.PLAYING
                } else {
                    state
                }
            }

        }
        playerRepository.changePlayerState(isPlaying)
    }

    override fun onUpdateTime(currentPosition: Int) {
        mediaPlay.updateTime(currentPosition)
    }

    private fun playerIsReady(): Boolean {
        return true
    }

    private fun playerIsStopped(): Boolean {
        return false
    }

    private fun playerIsResumed(): Boolean {
        return true
    }
}