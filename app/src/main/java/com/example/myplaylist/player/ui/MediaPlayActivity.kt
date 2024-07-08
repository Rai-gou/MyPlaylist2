package com.example.myplaylist.player.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.databinding.ActivityPlayerBinding
import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.DateTimeUtil
import com.example.myplaylist.search.ui.START_MEDIA_PUT_TRACK
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MediaPlayActivity : AppCompatActivity() {

    private val mediaPlayViewModel: MediaPlayViewModel by inject()
    private lateinit var binding: ActivityPlayerBinding
    private var playerStateJob: Job? = null
    private var currentTimeJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backPlayer.setOnClickListener {
            finish()
        }
        val track: Track? = intent.getParcelableExtra(START_MEDIA_PUT_TRACK)

        if (track != null) {
            mediaPlayViewModel.setTrack(track)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mediaPlayViewModel.track.collect { track ->
                    track?.let {
                        with(binding) {
                            textName.text = it.trackName
                            textBand.text = it.artistName
                            textTime.text = DateTimeUtil.simpleDateFormat(it.trackTimeMillis)
                            collectionName.text = it.collectionName
                            releaseDate.text = it.releaseDate.substringBefore("-")
                            primaryGenreName.text = it.primaryGenreName
                            textNameCountry.text = it.country
                        }
                        val url = it.previewUrl

                        val radiusInPx = resources.getDimensionPixelSize(R.dimen.top_margin)
                        Glide.with(this@MediaPlayActivity)
                            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                            .transform(RoundedCorners(radiusInPx))
                            .error(R.drawable.playplaceholder)
                            .into(binding.imagePlayer)
                    }
                }
            }
        }

        playerStateJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mediaPlayViewModel.playerState.collect { state ->
                    when (state) {
                        PlayerState.PLAY -> {
                            updatePlayButton(true)
                            Log.d("MyLog", "PlayerState is PLAY")
                        }
                        PlayerState.PAUSE -> {
                            updatePlayButton(false)
                            Log.d("MyLog", "PlayerState is PAUSE")
                        }
                    }
                }
            }
        }

        currentTimeJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mediaPlayViewModel.currentTime.collect { currentTime ->
                    binding.textPlay.text = currentTime
                }
            }
        }


        binding.buttonPlay.setOnClickListener {
            mediaPlayViewModel.playOrPause()
        }
    }

    private fun updatePlayButton(isPlaying: Boolean) {
        val imageId = if (isPlaying) R.drawable.button_pressed else R.drawable.play_button
        binding.buttonPlay.setImageResource(imageId)
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayViewModel.stop()
        updatePlayButton(false)
        Log.d("MyLog", "all stop")
        playerStateJob?.cancel()
        currentTimeJob?.cancel()
    }
}