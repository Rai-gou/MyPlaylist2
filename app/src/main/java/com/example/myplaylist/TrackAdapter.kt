package com.example.myplaylist
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    private val trackList = ArrayList<Track>()
    fun clearData() {
        trackList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int {
        return trackList.size
    }


    fun updateList(tracks: List<Track>) {
        clearData()
        trackList.addAll(tracks)
        notifyDataSetChanged()
    }
}

