package com.example.myplaylist.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.DateTimeUtil
import com.example.myplaylist.R
import com.example.myplaylist.domain.models.Track
import com.example.myplaylist.creator.Creator
import com.example.myplaylist.databinding.ActivityPlayerBinding
import com.example.myplaylist.domain.callback.MediaPlayCallback
import com.example.myplaylist.domain.use_case.MediaPlayerWrapper
import com.example.myplaylist.domain.playRepository.PlayerRepository
import com.example.myplaylist.domain.use_case.MediaPlayerUseCaseImpl
import com.example.myplaylist.domain.timeRpository.TimerState
import com.example.myplaylist.domain.timeRpository.TimerUpdate
import com.example.myplaylist.domain.use_case.TimerUseCaseImpl
import com.example.myplaylist.domain.playRepository.PlayerStateChangeListener
import com.example.myplaylist.domain.timeRpository.TimerRepository
import com.example.myplaylist.domain.use_case.MediaPlayerUseCase
import com.example.myplaylist.domain.use_case.PlayerInteractor
import com.example.myplaylist.domain.use_case.PlayerListener
import com.example.myplaylist.domain.use_case.TimerUseCase

const val CURRENT_TIME_MILLIS = 500

class MediaPlay : AppCompatActivity(), TimerUpdate, PlayerStateChangeListener, MediaPlayCallback {
    private lateinit var timerUseCase: TimerUseCase
    private lateinit var mediaPlayerUseCase: MediaPlayerUseCase
    private lateinit var playerInteractor: PlayerInteractor
    private lateinit var mediaPlayerWrapper: MediaPlayerWrapper
    private lateinit var timerRepository: TimerRepository
    private lateinit var playerListener: PlayerListener
    private lateinit var playerRepository: PlayerRepository

    companion object {
        private const val STATE_DEFAULT = 0
    }

    private var onPreparedListener: (() -> Unit)? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityPlayerBinding
    private var playerState = STATE_DEFAULT
    private lateinit var url: String
    private var isPlaying = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backPlayer.setOnClickListener {
            finish()
        }

        playerRepository = Creator.getPlayerRepository()
        isPlaying = playerRepository.isPlaying()

        timerRepository = Creator.getTimerRepository()
        timerUseCase = TimerUseCaseImpl(timerRepository)
        timerUseCase.startTimer()

        mediaPlayerWrapper = Creator.getMediaPlayerWrapper()
        mediaPlayerUseCase = MediaPlayerUseCaseImpl(mediaPlayerWrapper)
        playerInteractor = Creator.getPlayerInteractor()
        playerListener = Creator.getPlayerListener(this)

        playerRepository.addStateChangeListener(this)

        val track: Track? = intent.getParcelableExtra("track")

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
            Log.d("MyLog", "collectionName: ${it.collectionName}")
        }

        binding.buttonPlay.setOnClickListener {
            Log.d("MyLog", "buttonPlay: $playerState")
            if (isPlaying) {
                Log.d("MyLog", "buttonPlay: $playerState")
                mediaPlayerUseCase.pause()
                onStateChanged(isPlaying)
            } else {

                Log.d("MyLog", "buttonPlay else: $playerState")
                onPreparedListener = {
                    Log.d("MyLog", "onPreparedListener: $onPreparedListener")
                    onStateChanged(isPlaying)
                }
                preparePlayer()
            }
        }

        val radiusInPx = resources.getDimensionPixelSize(R.dimen.top_margin)

        Glide.with(this)
            .load(track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(RoundedCorners(radiusInPx))
            .error(R.drawable.playplaceholder)
            .into(binding.imagePlayer)
    }

    private fun preparePlayer() {
        mediaPlayerUseCase.setDataSource(url)
        Log.d("MyLog", "mediaPlayerUseCase: $url")
        mediaPlayerUseCase.prepareAsync {
            binding.buttonPlay.isEnabled = true
            Log.d("MyLog", "bindingEnabled: ${binding.buttonPlay.isEnabled.toString()}")
            onPreparedListener?.invoke()
            mediaPlayerWrapper.start()
            mediaPlayerUseCase.start()
            startUpdatingTime()
        }
        mediaPlayerUseCase.setOnCompletionListener {
            stopTimer()
            binding.textPlay.text = getString(R.string.timeFake)
            isPlaying = false
            updatePlayButton()
            mediaPlayerWrapper.seekToStart()
        }
    }

    private val updateTimeRunnable: Runnable = object : Runnable {
        override fun run() {
            if (isPlaying) {
                updateTimeText()
                handler.postDelayed(this, CURRENT_TIME_MILLIS.toLong())
            }
        }
    }

    private fun updateTimeText() {
        val currentSecond = mediaPlayerUseCase.currentPosition() + CURRENT_TIME_MILLIS
        val formattedTime = DateTimeUtil.simpleDateFormat(currentSecond.toLong())
        binding.textPlay.text = formattedTime
    }

    private fun startTimer() {
        handler.post(updateTimeRunnable)
    }

    private fun stopTimer() {
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun updatePlayButton() {
        val imageId = if (isPlaying) R.drawable.button_pressed else R.drawable.play_button
        binding.buttonPlay.setImageResource(imageId)
    }

    override fun onStateChanged(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        if (isPlaying) {
            startTimer()
        } else {
            stopTimer()
        }
        playerInteractor.stopUpdatingTime()
        this@MediaPlay.isPlaying = !isPlaying
        updatePlayButton()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerUseCase.pause()
        stopTimer()
        playerInteractor.startUpdatingTime(mediaPlayerUseCase.currentPosition())
    }

    override fun onDestroy() {
        super.onDestroy()
        timerUseCase.stopTimer()
        mediaPlayerUseCase.release()
        handler.removeCallbacksAndMessages(null)
    }

    override fun updateTime(currentPosition: Int) {
        val formattedTime = DateTimeUtil.simpleDateFormat(currentPosition.toLong())
        binding.textPlay.text = formattedTime
    }

    override fun startUpdatingTime() {
        handler.post(updateTimeRunnable)
    }

    override fun stopUpdatingTime() {
        handler.removeCallbacks(updateTimeRunnable)
    }

    override fun updateTimerState(state: TimerState) {
        when (state) {
            TimerState.START -> startTimer()
            TimerState.STOP -> stopTimer()
        }
    }
}