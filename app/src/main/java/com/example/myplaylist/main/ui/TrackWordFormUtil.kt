package com.example.myplaylist.main.ui

class TrackWordFormUtil {
    companion object {
        fun getTrackWordForm(count: Int): String {
            val lastDigit = count % 10
            val lastTwoDigits = count % 100

            return when {
                lastTwoDigits in 11..19 -> "треков"
                lastDigit == 1 -> "трек"
                lastDigit in 2..4 -> "трека"
                else -> "треков"
            }
        }
    }
}