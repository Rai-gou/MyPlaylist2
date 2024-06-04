package com.example.myplaylist.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylist.databinding.FragmentPlaylistsBinding
import org.koin.android.ext.android.inject

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val playlistFragmentViewModel: PlaylistFragmentViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }
}