package com.example.myplaylist.library.ui

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

    fun bind(playlist: PlaylistEntity) {
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
    }
}