package com.example.myplaylist


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.databinding.ActivityPlayerBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

const val CURRENT_TRACK = "KEY_CURRENT_TRACK"

class MediaPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBack = findViewById<ImageButton>(R.id.backPlayer)
        actionBack.setOnClickListener {
            finish()
        }

        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val trackTimeMillis = intent.getLongExtra("trackTimeMillis", 0)
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")
        val trackTimeFormatted =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
        val parts = releaseDate?.split("-")
        val datePart = parts?.get(0)

        fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

        binding.textName.text = trackName
        binding.textBand.text = artistName
        binding.textTime.text = trackTimeFormatted
        binding.collectionName.text = collectionName
        binding.releaseDate.text = datePart
        binding.primaryGenreName.text = primaryGenreName
        binding.textNameCountry.text = country

        Glide.with(this@MediaPlayer)
            .load(getCoverArtwork())
            .transform(RoundedCorners(8))
            .error(R.drawable.playplaceholder)
            .into(binding.imagePlayer)

    }


}