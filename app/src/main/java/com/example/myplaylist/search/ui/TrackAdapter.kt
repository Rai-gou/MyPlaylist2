package com.example.myplaylist.search.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.databinding.ActivityTrackBinding
import com.example.myplaylist.player.model.Track

class TrackAdapter(
    private val context: SearchFragment,
    private val itemClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var tracks: List<Track> = emptyList()

    fun setData(newTracks: List<Track>) {
        tracks = newTracks
        Log.d("MyLog", "tracks: $tracks")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityTrackBinding.inflate(inflater, parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track, itemClickListener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}