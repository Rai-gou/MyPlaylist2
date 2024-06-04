package com.example.myplaylist.library.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myplaylist.databinding.ActivityMedialibraryBinding
import org.koin.android.ext.android.inject

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialibraryBinding
    private lateinit var mediaLibraryAdapter: MediaLibraryAdapter
    private val mediaLibraryActivityViewModel: MediaLibraryActivityViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaLibraryAdapter = MediaLibraryAdapter(this)
        binding.viewPagerMediaLibrary.adapter = mediaLibraryAdapter

        mediaLibraryActivityViewModel.setupTabLayoutMediator(binding.tabLayoutMediaLibrary, binding.viewPagerMediaLibrary)

        binding.imageButtonBackLibrary.setOnClickListener {
            finish()
        }
    }
}