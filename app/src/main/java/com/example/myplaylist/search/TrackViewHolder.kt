package com.example.myplaylist.search

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.R
import com.example.myplaylist.databinding.ActivityTrackBinding
import com.example.myplaylist.search.ui.DateTimeUtil
import com.example.myplaylist.player.model.Track

class TrackViewHolder(
    private val binding: ActivityTrackBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track, itemClickListener: (Track) -> Unit) {
        binding.apply {
            titleTextView.text = track.trackName
            artistTextView.text = track.artistName
            val trackTimeFormatted = DateTimeUtil.simpleDateFormat(track.trackTimeMillis)
            timeTextView.text = trackTimeFormatted

            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .transform(RoundedCorners(2))
                .error(R.drawable.placeholder)
                .into(imageTrack)

            root.setOnClickListener {
                itemClickListener(track)
            }
        }
    }
}