package com.example.myplaylist.player.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylist.R
import com.example.myplaylist.databinding.ActivityPlayerBinding
import com.example.myplaylist.library.ui.NewPlaylist.NewPlaylistFragment
import com.example.myplaylist.player.model.PlayerState
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.ui.DateTimeUtil
import com.example.myplaylist.search.ui.START_MEDIA_PUT_TRACK
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MediaPlayActivity : AppCompatActivity() {

    private val mediaPlayViewModel: MediaPlayViewModel by inject()
    private lateinit var binding: ActivityPlayerBinding
    private var playerStateJob: Job? = null
    private var currentTimeJob: Job? = null
    private var adapter: MediaPlayAdapter? = null
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
            mediaPlayViewModel.checkTrackIsFavorite(track)
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
        mediaPlayViewModel.isTrackFavorite.observe(this) { isFavorite ->
            track?.let {
                updateFavoriteButton(isFavorite)
            }
        }
        binding.buttonOnFavorite.setOnClickListener {
            track?.let {
                if (mediaPlayViewModel.isTrackFavorite.value == true) {
                    mediaPlayViewModel.deleteTrackOnFavorite(it)
                    Log.d("buttonOnFavorite", "if")
                } else {
                    mediaPlayViewModel.saveTrackOnFavorite(it)
                    Log.d("buttonOnFavorite", "else")
                }

            }
        }
        val bottomSheetContainer = findViewById<LinearLayout>(R.id.trackAdd)

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("bottomSheetBehavior", "onSlide")
            }
        })
        binding.buttonAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.buttonAddTrack.setOnClickListener {
            openNewPlaylistFragment()
            refreshPlaylists()
        }
        adapter = MediaPlayAdapter(emptyList(), BottomSheetBehavior.from(binding.trackAdd)) { playlist ->
            track?.let { track ->
                mediaPlayViewModel.addTrackToPlaylist(playlist.id, track)
            }
        }
        binding.trackAddRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.trackAddRecyclerView.adapter = adapter

        mediaPlayViewModel.trackAddStatus.observe(this) { status ->
            val (wasAdded, playlistName) = status
            if (wasAdded) {
                BottomSheetBehavior.from(binding.trackAdd).state = BottomSheetBehavior.STATE_HIDDEN
                showToastInPlaylist(playlistName)
            } else {
                showToastOnPlaylist(playlistName)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mediaPlayViewModel.allPlaylists.collect { playlists ->
                    adapter?.updateData(playlists)
                }
            }
        }
        supportFragmentManager.setFragmentResultListener("newPlaylistRequestKey", this) { requestKey, bundle ->
            if (requestKey == "newPlaylistRequestKey") {
                refreshPlaylists()
            }
        }

        refreshPlaylists()
    }

    private fun openNewPlaylistFragment() {
        val newPlaylistFragment = NewPlaylistFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, newPlaylistFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun refreshPlaylists() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mediaPlayViewModel.refreshPlaylists()
            }
        }
    }
    private fun updatePlayButton(isPlaying: Boolean) {
        val imageIdPlay = if (isPlaying) R.drawable.button_pressed else R.drawable.play_button
        binding.buttonPlay.setImageResource(imageIdPlay)
    }
    private fun updateFavoriteButton(onFavorite: Boolean) {
        val imageIdFavorite = if (onFavorite) R.drawable.button_favorite else R.drawable.like_button
        binding.buttonOnFavorite.setImageResource(imageIdFavorite)
    }

    private fun showToastInPlaylist(name: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.fragment_toast, null)

        val text: TextView = layout.findViewById(R.id.toastText)
        text.text = getString(R.string.playlist_added_toast, name)

        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
    private fun showToastOnPlaylist(name: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.fragment_toast, null)

        val text: TextView = layout.findViewById(R.id.toastText)
        text.text = getString(R.string.track_already_added_toast, name)

        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
    override fun onResume() {
        super.onResume()
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