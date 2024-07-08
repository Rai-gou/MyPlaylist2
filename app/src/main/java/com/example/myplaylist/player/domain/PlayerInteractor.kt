package com.example.myplaylist.player.domain

import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.PlayerStatus
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlayerInteractor {
    val playerStateFlow: Flow<PlayerState>
    val track: StateFlow<Track>
    val currentTimeFlow: Flow<String>

    fun setTrack(track: Track)
    fun playOrPause()
    fun seekTo(position: Int)
    fun getCurrentPosition(): Int
    fun updateTime(currentPosition: Int)
    fun onTimeUpdate(currentPosition: Int)
    fun setResetTimer()
    fun stopPlayer()
    fun stopPlayback()
}