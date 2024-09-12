package com.example.myplaylist.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.example.myplaylist.main.ui.TrackWordFormUtil

class MediaPlayPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val titlePlaylist: TextView = itemView.findViewById(R.id.titleTextPlaylistMediaTrack)
    private val titleListTracks: TextView = itemView.findViewById(R.id.listMediaTrack)
    private val imagePlaylist: ImageView = itemView.findViewById(R.id.imageTrackPlaylist)

    fun bind(playlist: NewPlaylistWithTracks, onClick: (NewPlaylistWithTracks) -> Unit) {
        titlePlaylist.text = playlist.name
        titleListTracks.text = "${playlist.trackCount} ${TrackWordFormUtil.getTrackWordForm(playlist.trackCount)}"

        if (playlist.previewUrl.isNotEmpty()) {
            Glide.with(itemView.context)
                .load(playlist.previewUrl)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(8))
                .into(imagePlaylist)
        } else {
            Glide.with(itemView.context)
                .load(R.drawable.placeholder)
                .transform(RoundedCorners(8))
                .into(imagePlaylist)
        }

        itemView.setOnClickListener {
            onClick(playlist)
        }
    }
}