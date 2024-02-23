package com.example.myplaylist

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.ui.MediaPlay


import java.util.ArrayList

const val START_MEDIA_PUT_TRACK = "track"

class TrackAdapter(private val context: Context) : RecyclerView.Adapter<TrackViewHolder>() {
    private val trackList = ArrayList<Track>()
    val historyList = ArrayList<Track>()
    var isShowingTrackList: Boolean = true
    private var isShowingPlayer: Boolean = false
    private lateinit var itemClickListener: OnItemClickListener

    private val typeTrack = 1
    private val typePlayer = 2
    fun clearData() {
        trackList.clear()
        notifyDataSetChanged()
    }

    fun clearHistory() {
        historyList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return when (viewType) {
            typeTrack -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.activity_track, parent, false)
                TrackViewHolder(view, itemClickListener)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        val currentList = if (isShowingTrackList) trackList else historyList
        val track = currentList[position] as Track
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        val currentList = if (isShowingTrackList) trackList else historyList
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShowingPlayer) {
            typePlayer
        } else {
            typeTrack
        }
    }

    fun updateList(tracks: List<Track>) {
        clearData()
        isShowingTrackList = true
        trackList.addAll(tracks)
        notifyDataSetChanged()
    }

    fun updateHistoryList(tracks: List<Track>) {
        isShowingTrackList = false
        isShowingPlayer = false
        historyList.addAll(tracks)
        notifyDataSetChanged()
    }

    fun getHistoryList(): List<Track> {
        isShowingTrackList = false
        isShowingPlayer = false
        return ArrayList(historyList)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun getItem(position: Int): Track {
        val currentList = if (isShowingTrackList) trackList else historyList
        Log.d("MyLog", "position: $position")
        return currentList[position]
    }

    fun startMediaPlayerActivity(track: Track) {
        val intent = Intent(context, MediaPlay::class.java)
        intent.putExtra(START_MEDIA_PUT_TRACK, track)
        context.startActivity(intent)
    }
}