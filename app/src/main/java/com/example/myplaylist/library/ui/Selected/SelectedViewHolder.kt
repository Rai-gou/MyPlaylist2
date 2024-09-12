package com.example.myplaylist.library.ui.Selected

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.DateTimeUtil

class SelectedViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.activity_track, parent, false)
    ) {

    private val trackNameTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artistTextView)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.timeTextView)
    private val artImageView: ImageView = itemView.findViewById(R.id.imageTrack)
    private val root: View = itemView

    fun bind(track: Track, itemClickListener: (Track) -> Unit) {

        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        trackTimeTextView.text = DateTimeUtil.simpleDateFormat(track.trackTimeMillis)
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2))
            .error(R.drawable.placeholder)
            .into(artImageView)

        root.setOnClickListener {
            itemClickListener(track)
        }
    }
}