package com.example.myplaylist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Log

class TrackViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val trackNameTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val artistNameTextView: TextView = itemView.findViewById(R.id.artistTextView)
    private val trackTimeTextView: TextView = itemView.findViewById(R.id.timeTextView)
    private val artImageView: ImageView = itemView.findViewById(R.id.imageTrack)

    fun bind(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        val trackTimeFormatted = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackTimeTextView.text = trackTimeFormatted

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2))
            .error(R.drawable.placeholder)
            .into(artImageView)
           }
}