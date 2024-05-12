package com.example.myplaylist.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.creator.Creator
import com.example.myplaylist.databinding.ActivityPlayerBinding
import com.example.myplaylist.player.domain.PlayerInteractor
import com.example.myplaylist.player.domain.PlayerInteractorImpl
import com.example.myplaylist.player.domain.use_case.MediaPlayerUseCase
import com.example.myplaylist.player.domain.use_case.MediaPlayerUseCaseImpl
import com.example.myplaylist.player.domain.use_case.TimerUseCase
import com.example.myplaylist.player.domain.use_case.TimerUseCaseImpl
import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.DateTimeUtil
import com.example.myplaylist.search.ui.START_MEDIA_PUT_TRACK

class MediaPlayActivity : AppCompatActivity() {

    private lateinit var timerUseCase: TimerUseCase
    private lateinit var playerInteractor: PlayerInteractor
    private lateinit var mediaPlayViewModel: MediaPlayViewModel
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var url: String
    lateinit var mediaPlayerUseCase: MediaPlayerUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backPlayer.setOnClickListener {
            finish()
        }

        val mediaPlayerWrapper = Creator.getMediaPlayerWrapperImpl()
        mediaPlayerUseCase = MediaPlayerUseCaseImpl(mediaPlayerWrapper)

        playerInteractor = PlayerInteractorImpl()

        val track: Track? = intent.getParcelableExtra(START_MEDIA_PUT_TRACK)

        timerUseCase = TimerUseCaseImpl()

        mediaPlayViewModel = ViewModelProvider(
            this,
            MediaPlayViewModel.getViewModelFactory(
                mediaPlayerUseCase,
                timerUseCase,
                playerInteractor
            )
        )[MediaPlayViewModel::class.java]

        if (track != null) {
            mediaPlayViewModel.setTrack(track)
        }

        mediaPlayViewModel.track.observe(this) { track ->
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
                url = it.previewUrl

                val radiusInPx = resources.getDimensionPixelSize(R.dimen.top_margin)
                Glide.with(this@MediaPlayActivity)
                    .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                    .transform(RoundedCorners(radiusInPx))
                    .error(R.drawable.playplaceholder)
                    .into(binding.imagePlayer)
            }
        }

        mediaPlayViewModel.playerState.observe(this) { state ->
            when (state) {
                PlayerState.PLAY -> {
                    updatePlayButton(true)
                    mediaPlayViewModel.startUpdatingTime()
                }

                PlayerState.PAUSE -> {
                    updatePlayButton(false)
                    mediaPlayViewModel.stopUpdatingTime()
                }

                else -> {}
            }
        }


        mediaPlayViewModel.currentTime.observe(this) { currentTime ->
            binding.textPlay.text = currentTime
        }

        binding.buttonPlay.setOnClickListener {
            mediaPlayViewModel.playOrPause()
        }
    }

    private fun updatePlayButton(isPlaying: Boolean) {
        val imageId = if (isPlaying) R.drawable.button_pressed else R.drawable.play_button
        binding.buttonPlay.setImageResource(imageId)
    }

    override fun onStop() {
        super.onStop()
        mediaPlayViewModel.stopPlayback()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayViewModel.stopUpdatingTime()
        mediaPlayViewModel.stopPlayback()
    }
}