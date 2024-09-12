package com.example.myplaylist.library.ui.Playlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.R
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import kotlin.math.log
class PlaylistFragmentAdapter(
    private val onPlaylistClick: (NewPlaylistWithTracks) -> Unit
) : RecyclerView.Adapter<PlaylistFragmentViewHolder>() {

    private val playlists = mutableListOf<NewPlaylistWithTracks>()

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

    fun updatePlaylists(newPlaylists: List<NewPlaylistWithTracks>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }

    fun updateSinglePlaylist(updatedPlaylist: NewPlaylistWithTracks) {
        val index = playlists.indexOfFirst { it.id == updatedPlaylist.id }
        if (index != -1) {
            playlists[index] = updatedPlaylist
            notifyItemChanged(index)
        } else {
            playlists.add(updatedPlaylist)
            notifyItemInserted(playlists.size - 1)
        }
        notifyDataSetChanged()
    }
}