package com.example.myplaylist


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale


const val CURRENT_TRACK = "KEY_CURRENT_TRACK"
const val CURRENT_TIME = 500

@Suppress("DEPRECATION")
class MediaPlay : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
    }
    private val currentNightMode = AppCompatDelegate.getDefaultNightMode()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var artworkUrl100: String
    private var playerState = STATE_DEFAULT
    private lateinit var play: AppCompatImageButton
    private var mediaPlayer = MediaPlayer()
    private lateinit var url: String
    private lateinit var timer: TextView
    private var onPreparedListener: (() -> Unit)? = null
    private var isPlaying = false
    private var imageId = R.drawable.play_button
        set(value) {
            field = value
            play.setImageResource(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBack = findViewById<ImageButton>(R.id.backPlayer)
        actionBack.setOnClickListener {
            finish()
        }
        timer = findViewById(R.id.textPlay)
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

        preparePlayer()
        play = findViewById(R.id.buttonPlay)
        play.setOnClickListener {

            Log.d("MyLog", "while: $playerState")
            if (playerState == STATE_PREPARED) {
                playbackControl()
                Log.d("MyLog", "whileEnd: $playerState")

            } else {
                onPreparedListener = {
                    playbackControl()
                }
            }
        }

        fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        val radiusInDp = 8
        val radiusInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, radiusInDp.toFloat(), resources.displayMetrics
        ).toInt()

        Glide.with(this@MediaPlay).load(getCoverArtwork()).transform(RoundedCorners(radiusInPx))
            .error(R.drawable.playplaceholder).into(binding.imagePlayer)


    }

    private val updateTime: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                updateTimeText()
                handler.postDelayed(this, 500)
            }
        }
    }

    private fun updateTimeText() {
        val formattedTime =
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition + CURRENT_TIME)
        timer.text = formattedTime
    }

    private fun startTimer() {
        handler.post(updateTime)
    }

    private fun stopTimer() {
        handler.removeCallbacks(updateTime)
    }


    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
            Log.d("MyLog", "Enabled: $playerState")

            onPreparedListener?.invoke()
            onPreparedListener = null
        }
        mediaPlayer.setOnCompletionListener {
            stopTimer()
            imageId =
                if (currentNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
                    R.drawable.play_button
                } else {
                    R.drawable.white_play_button
                }
            timer.text = "00:00"
            isPlaying = false
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        isPlaying = true
        startTimer()
        updatePlayButton()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        isPlaying = false
        stopTimer()
        updatePlayButton()
    }
    private fun updatePlayButton() {
        imageId = if (isPlaying) {
            if (currentNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
                R.drawable.button_pressed
            } else {
                R.drawable.button_night_pressed
            }
        } else {
            if (currentNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
                R.drawable.play_button
            } else {
                R.drawable.white_play_button
            }
        }
    }
    private fun playbackControl() {
        Log.d("MyLog", "collectionName: $isPlaying")
        Log.d("MyLog", "time: ${timer.text}")

        if (isPlaying) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        imageId = if (currentNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            R.drawable.play_button
        } else {
            R.drawable.white_play_button
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        mediaPlayer.release()
    }

}