package com.example.myplaylist.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.R
import com.example.myplaylist.library.db.PlaylistEntity

class PlaylistFragmentAdapter(private val playlists: List<PlaylistEntity>) : RecyclerView.Adapter<PlaylistFragmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_playlist, parent, false)
        return PlaylistFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistFragmentViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}