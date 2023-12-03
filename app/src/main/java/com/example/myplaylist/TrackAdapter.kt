package com.example.myplaylist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    private val trackList = ArrayList<Track>()
    val historyList = ArrayList<Track>()
    private var isShowingTrackList: Boolean = true

    fun clearData() {
        trackList.clear()
        notifyDataSetChanged()
    }

    fun clearHistory() {
        historyList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentList = if (isShowingTrackList) trackList else historyList
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        val currentList = if (isShowingTrackList) trackList else historyList
        return currentList.size
    }

    fun updateList(tracks: List<Track>) {
        clearData()
        isShowingTrackList = true
        trackList.addAll(tracks)
        notifyDataSetChanged()
    }

    fun updateHistory(tracks: List<Track>) {
        isShowingTrackList = false
        notifyDataSetChanged()
    }

    fun getHistoryList(): List<Track> {
        return ArrayList(historyList)
    }

    fun getTrackList(): List<Track> {
        return ArrayList(trackList)
    }

}

