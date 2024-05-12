package com.example.myplaylist.search.domain

import android.content.SharedPreferences
import android.util.Log
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.data.HistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SHARED_KEY_TRACK = "KEY_TRACK"
const val MAX_LIST_SIZE = 10

class HistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) : HistoryRepository {

    private val gson = Gson()

    override fun saveHistoryTrack(track: Track) {
        val historyList = loadHistoryTracks().toMutableList()

        val trackIndex = historyList.indexOfFirst { it.trackId == track.trackId }
        if (trackIndex != -1) {
            historyList.removeAt(trackIndex)
        }
        if (historyList.size >= MAX_LIST_SIZE) {
            historyList.add(0, track)
            historyList.removeAt(historyList.size - 1)
        } else {
            historyList.add(0, track)
        }
        Log.d("MyLog", "historyList: $historyList")
        saveHistoryList(historyList)
    }

    override fun loadHistoryTracks(): List<Track> {
        return loadHistoryList()
    }

    override fun clearHistory() {
        saveHistoryList(emptyList())
    }

    private fun saveHistoryList(historyList: List<Track>) {
        val json = Gson().toJson(historyList)
        sharedPreferences.edit().apply {
            putString(SHARED_KEY_TRACK, json)
            apply()
        }
    }

    private fun loadHistoryList(): List<Track> {
        val json = sharedPreferences.getString(SHARED_KEY_TRACK, null)
        return if (json != null) {
            val type = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }
}