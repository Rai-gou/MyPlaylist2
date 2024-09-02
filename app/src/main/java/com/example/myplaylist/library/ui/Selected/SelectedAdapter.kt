package com.example.myplaylist.library.ui.Selected

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.player.model.Track

class SelectedAdapter(private val itemClickListener: (Track) -> Unit) :
    RecyclerView.Adapter<SelectedViewHolder>() {
    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedViewHolder =
        SelectedViewHolder(parent)

    override fun onBindViewHolder(holder: SelectedViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track, itemClickListener)
    }

    override fun getItemCount(): Int = tracks.size
}