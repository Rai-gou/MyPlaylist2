package com.example.myplaylist.library.ui.Playlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.db.PlaylistEntity

class PlaylistFragmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val titlePlaylist: TextView = itemView.findViewById(R.id.titleTextPlaylist)
    private val titleListTracks: TextView = itemView.findViewById(R.id.titleListTracks)
    private val imagePlaylist: ImageView = itemView.findViewById(R.id.imagePlaylist)

    fun bind(playlist: NewPlaylist) {
        titlePlaylist.text = playlist.name
        titleListTracks.text = "Tracks: ${playlist.trackCount}"

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
    }
}