package com.example.myplaylist


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.databinding.ActivityPlayerBinding

const val CURRENT_TIME_MILLIS = 500
@Suppress("DEPRECATION")
class MediaPlay : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
    }
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityPlayerBinding
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var url: String
    private var onPreparedListener: (() -> Unit)? = null
    private var isPlaying = false
    private var imageId = R.drawable.play_button
        set(value) {
            field = value
            binding.buttonPlay.setImageResource(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backPlayer.setOnClickListener {
            finish()
        }

        val track: Track? = intent.getParcelableExtra("track")

        track?.let {
            binding.textName.text = it.trackName
            binding.textBand.text = it.artistName
            val trackTimeFormatted = DateTimeUtil.simpleDateFormat(it.trackTimeMillis)
            binding.textTime.text = trackTimeFormatted
            binding.collectionName.text = it.collectionName
            binding.releaseDate.text = it.releaseDate.substringBefore("-")
            binding.primaryGenreName.text = it.primaryGenreName
            binding.textNameCountry.text = it.country
            url = it.previewUrl
            Log.d("MyLog", "collectionName: ${it.collectionName}")
        }

        preparePlayer()

        binding.buttonPlay.setOnClickListener {

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

        val radiusInPx = resources.getDimensionPixelSize(R.dimen.top_margin)

        Glide.with(this@MediaPlay)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(RoundedCorners(radiusInPx))
            .error(R.drawable.playplaceholder)
            .into(binding.imagePlayer)
    }

    private val updateTimeRunnable: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                updateTimeText()
                handler.postDelayed(this, CURRENT_TIME_MILLIS.toLong())
            }
        }
    }

    private fun updateTimeText() {
        val currentSecond = mediaPlayer.currentPosition + CURRENT_TIME_MILLIS
        val formattedTime = DateTimeUtil.simpleDateFormat(currentSecond.toLong())
        binding.textPlay.text = formattedTime
    }

    private fun startTimer() {
        handler.post(updateTimeRunnable)
    }

    private fun stopTimer() {
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
            Log.d("MyLog", "Enabled: $playerState")

            onPreparedListener?.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            stopTimer()
            binding.textPlay.text = getString(R.string.timeFake)
            isPlaying = false
            updatePlayButton()
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
                R.drawable.button_pressed
        } else {
                R.drawable.play_button
        }
    }
    private fun playbackControl() {
        Log.d("MyLog", "collectionName: $isPlaying")
        Log.d("MyLog", "time: ${binding.textPlay.text}")

        if (isPlaying) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }
}