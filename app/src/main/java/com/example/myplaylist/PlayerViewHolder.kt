package com.example.myplaylist

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewHolder (item: View, private val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(item) {

    init {
        item.setOnClickListener {
            itemClickListener.onItemClick(adapterPosition)
        }
    }
    private val trackNameText: TextView = itemView.findViewById(R.id.textName)
    private val artistNameText: TextView = itemView.findViewById(R.id.textBand)
    private val trackTimeText: TextView = itemView.findViewById(R.id.textTime)
    private val artImage: ImageView = itemView.findViewById(R.id.imagePlayer)
    private val collectionNameText: TextView = itemView.findViewById(R.id.collectionName)
    private val releaseDateText: TextView =itemView.findViewById(R.id.releaseDate)
    private val primaryGenreNameText: TextView = itemView.findViewById(R.id.primaryGenreName)
    private val countryText: TextView = itemView.findViewById(R.id.textNameCountry)

    fun bind(track: Track) {
        trackNameText.text = track.trackName
        artistNameText.text = track.artistName
        val trackTimeFormatted = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackTimeText.text = trackTimeFormatted
        collectionNameText.text = track.collectionName
        releaseDateText.text = track.releaseDate
        primaryGenreNameText.text = track.primaryGenreName
        countryText.text = track.country

        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2))
            .error(R.drawable.placeholder)
            .into(artImage)
    }
}