package com.example.myplaylist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.ArrayList

const val SHARED_KEY_TRACK = "KEY_TRACK"

class SearchHistory(
    private val sharedPreferencesTrack: SharedPreferences,
    private val adapter: TrackAdapter

) : Serializable {

    private val gson = Gson()

    fun saveHistoryTrack(track: Track) {

        val historyList = adapter.historyList

        if (adapter.isShowingTrackList) {
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
        }

        adapter.startMediaPlayerActivity(track)
        saveHistoryList(historyList)
        Log.d("MyLog", "saveHistoryList: $historyList")
    }


    private fun saveHistoryList(historyList: List<Track>) {
        val json = gson.toJson(historyList)
        sharedPreferencesTrack.edit {
            putString(SHARED_KEY_TRACK, json)
        }
    }

    fun loadHistoryList(): List<Track> {
        val json = sharedPreferencesTrack.getString(SHARED_KEY_TRACK, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }
}