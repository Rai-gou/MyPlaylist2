package com.example.myplaylist.library.ui.MediaLibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylist.R
import com.example.myplaylist.databinding.FragmentMedialibraryBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject

class MediaLibraryFragment : Fragment() {

    private var _binding: FragmentMedialibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaLibraryAdapter: MediaLibraryAdapter
    private lateinit var tabMediator: TabLayoutMediator
    private val mediaLibraryActivityViewModel: MediaLibraryActivityViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaLibraryAdapter = MediaLibraryAdapter(this)
        binding.viewPagerMediaLibrary.adapter = mediaLibraryAdapter

        tabMediator = TabLayoutMediator(
            binding.tabLayoutMediaLibrary,
            binding.viewPagerMediaLibrary
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}