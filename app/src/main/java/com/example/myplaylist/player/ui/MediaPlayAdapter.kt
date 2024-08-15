package com.example.myplaylist.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.R
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.db.PlaylistEntity
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MediaPlayAdapter(
    private var playlists: List<NewPlaylist>,
    private val bottomSheetBehavior: BottomSheetBehavior<LinearLayout>,
    private val onPlaylistClick: (NewPlaylist) -> Unit
) : RecyclerView.Adapter<MediaPlayPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPlayPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_new_trackplaylist, parent, false)
        return MediaPlayPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaPlayPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position]) { playlist ->
            onPlaylistClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updateData(newPlaylists: List<NewPlaylist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}