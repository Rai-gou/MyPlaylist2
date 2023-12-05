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
    private lateinit var itemClickListener: OnItemClickListener

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
        return TrackViewHolder(view, itemClickListener)
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

    fun getHistoryList(): List<Track> {
        isShowingTrackList = false
        return ArrayList(historyList)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun getItem(position: Int): Track {
        val currentList = if (isShowingTrackList) trackList else historyList
        return currentList[position]
    }
}

