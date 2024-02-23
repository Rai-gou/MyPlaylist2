package com.example.myplaylist.domain.use_case

import com.example.myplaylist.domain.models.PlayerState

interface PlayerListener {
    fun onChange(state: PlayerState)
    fun onUpdateTime(currentPosition: Int)
}