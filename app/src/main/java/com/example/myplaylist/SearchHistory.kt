package com.example.myplaylist

import android.content.SharedPreferences

import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import java.io.Serializable
import java.util.ArrayList

const val SHARED_KEY_TRACK = "KEY_TRACK"

class SearchHistory(
    private val sharedPreferences: SharedPreferences,
    private val adapter: TrackAdapter
) : Serializable {

    private val gson = Gson()

    fun saveHistoryTrack(track: Track) {
        val historyList = adapter.historyList
        Log.d("MyLog", "OneList: $historyList")
        val trackIndex = historyList.indexOfFirst { it.trackId == track.trackId }

        if (trackIndex != -1) {
            historyList.removeAt(trackIndex)
        }
        if (historyList.size >= 10) {
            historyList.add(0, track)
            Log.d("MyLog", "size: ${historyList.size}")
            historyList.removeAt(historyList.size - 1)
        } else {
            historyList.add(0, track)
        }

        saveHistoryList(historyList)

        Log.d("MyLog", "saveHistoryList: $historyList")
    }

    private fun saveHistoryList(historyList: List<Track>) {
        val json = gson.toJson(historyList)
        sharedPreferences.edit {
            putString(SHARED_KEY_TRACK, json)
        }
    }

}