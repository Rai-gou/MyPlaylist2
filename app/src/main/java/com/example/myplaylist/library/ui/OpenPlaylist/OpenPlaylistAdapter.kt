package com.example.myplaylist.library.ui.OpenPlaylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.R
import com.example.myplaylist.databinding.ActivityTrackBinding
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.TrackViewHolder

class OpenPlaylistAdapter(
    private val tracks: List<Track>,
    private val onTrackClick: (Track) -> Unit,
    private val onTrackLongClick: (Track) -> Unit
) : RecyclerView.Adapter<OpenPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenPlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityTrackBinding.inflate(inflater, parent, false)
        return OpenPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OpenPlaylistViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track, onTrackClick)

        holder.itemView.setOnClickListener {
            onTrackClick(track)
        }

        holder.itemView.setOnLongClickListener {
            onTrackLongClick(track)
            true
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}