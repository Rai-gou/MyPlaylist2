package com.example.myplaylist.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.library.db.PlaylistEntity

class MediaPlayPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val titlePlaylist: TextView = itemView.findViewById(R.id.titleTextPlaylistMediaTrack)
    private val titleListTracks: TextView = itemView.findViewById(R.id.listMediaTrack)
    private val imagePlaylist: ImageView = itemView.findViewById(R.id.imageTrackPlaylist)

    fun bind(playlist: PlaylistEntity, onClick: (PlaylistEntity) -> Unit) {
        titlePlaylist.text = playlist.playlistName
        titleListTracks.text = "Tracks: ${playlist.trackCount}"

        if (playlist.previewUrlList.isNotEmpty()) {
            Glide.with(itemView.context)
                .load(playlist.previewUrlList)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(2))
                .into(imagePlaylist)
        } else {
            imagePlaylist.setImageResource(R.drawable.placeholder)
        }

        itemView.setOnClickListener {
            onClick(playlist)
        }
    }
}