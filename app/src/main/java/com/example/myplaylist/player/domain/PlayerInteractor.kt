package com.example.myplaylist.player.domain

import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.PlayerStatus
import com.example.myplaylist.player.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlayerInteractor {
    val playerStateFlow: Flow<PlayerState>
    val track: StateFlow<Track>
    val currentTimeFlow: Flow<String>

    suspend fun setTrack(track: Track)
    suspend fun playOrPause()
    suspend fun seekTo(position: Int)
    fun getCurrentPosition(): Int
    fun updateTime(currentPosition: Int)
    fun onTimeUpdate(currentPosition: Int)
    suspend fun setResetTimer()
    suspend fun stopPlayer()
    suspend fun stopPlayback()
}