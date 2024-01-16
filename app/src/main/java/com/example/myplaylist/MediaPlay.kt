package com.example.myplaylist


import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

const val CURRENT_TRACK = "KEY_CURRENT_TRACK"

@Suppress("DEPRECATION")
class MediaPlay : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var artworkUrl100: String
    private var playerState = STATE_DEFAULT
    private lateinit var play: AppCompatImageButton
    private var mediaPlayer = MediaPlayer()
    private lateinit var url: String
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
            url = it.previewUrl
            Log.d("MyLog", "collectionName: ${it.collectionName}")
            Log.d("MyLog", "url1: ${it.previewUrl}")
        }

        play = findViewById(R.id.buttonPlay)
        preparePlayer()
        play.setOnClickListener {
            playbackControl()
        }
        fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        val radiusInDp = 8
        val radiusInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, radiusInDp.toFloat(), resources.displayMetrics
        ).toInt()

        Glide.with(this@MediaPlay).load(getCoverArtwork()).transform(RoundedCorners(radiusInPx))
            .error(R.drawable.playplaceholder).into(binding.imagePlayer)

    }

    private fun preparePlayer() {
        Log.d("MyLog", "url: $url")
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}