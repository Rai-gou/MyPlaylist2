package com.example.myplaylist.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myplaylist.R
import com.example.myplaylist.databinding.ActivityMedialibraryBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialibraryBinding
    private lateinit var mediaLibraryAdapter: MediaLibraryAdapter
    private lateinit var tabMediator: TabLayoutMediator
    private val mediaLibraryActivityViewModel: MediaLibraryActivityViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaLibraryAdapter = MediaLibraryAdapter(this)
        binding.viewPagerMediaLibrary.adapter = mediaLibraryAdapter

        tabMediator = TabLayoutMediator(binding.tabLayoutMediaLibrary, binding.viewPagerMediaLibrary) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        binding.imageButtonBackLibrary.setOnClickListener {
            finish()
        }
    }
    override fun onDestroy() {
        tabMediator.detach()
        super.onDestroy()
    }
}