package com.example.myplaylist


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.databinding.ActivityPlayerBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

const val CURRENT_TRACK = "KEY_CURRENT_TRACK"

@Suppress("DEPRECATION")
class MediaPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var artworkUrl100: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBack = findViewById<ImageButton>(R.id.backPlayer)
        actionBack.setOnClickListener {
            finish()
        }




        val track: Track? = intent.getParcelableExtra("track")

        track?.let {
            binding.textName.text = it.trackName
            binding.textBand.text = it.artistName
            val trackTimeFormatted =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis)
            binding.textTime.text = trackTimeFormatted
            binding.collectionName.text = it.collectionName
            binding.releaseDate.text = it.releaseDate.substringBefore("-")
            binding.primaryGenreName.text = it.primaryGenreName
            binding.textNameCountry.text = it.country
            artworkUrl100 = it.artworkUrl100

        }

        fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        val radiusInDp = 8
        val radiusInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            radiusInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        Glide.with(this@MediaPlayer)
            .load(getCoverArtwork())
            .transform(RoundedCorners(radiusInPx))
            .error(R.drawable.playplaceholder)
            .into(binding.imagePlayer)

    }
}