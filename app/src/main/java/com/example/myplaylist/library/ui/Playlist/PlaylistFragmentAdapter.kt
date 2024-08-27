package com.example.myplaylist.library.ui.Playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.R
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.db.PlaylistEntity

class PlaylistFragmentAdapter(
    private val onPlaylistClick: (NewPlaylist) -> Unit
) : RecyclerView.Adapter<PlaylistFragmentViewHolder>() {

    private val playlists = mutableListOf<NewPlaylist>() // Храним список плейлистов в mutableList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistFragmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_list_playlist, parent, false)
        return PlaylistFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistFragmentViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { onPlaylistClick(playlist) }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updatePlaylists(newPlaylists: List<NewPlaylist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}