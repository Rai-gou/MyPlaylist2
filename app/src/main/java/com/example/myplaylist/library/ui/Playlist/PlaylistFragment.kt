package com.example.myplaylist.library.ui.Playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myplaylist.R
import com.example.myplaylist.databinding.FragmentPlaylistsBinding
import com.example.myplaylist.library.data.NewPlaylistWithTracks
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playlistId: String? by lazy {
        arguments?.getString("playlistId")
    }

    private val playlistFragmentViewModel: PlaylistFragmentViewModel by inject()

    private val adapter by lazy {
        PlaylistFragmentAdapter { playlist -> openPlaylist(playlist) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNewPlaylist.setOnClickListener {
            openNewPlaylistFragment()
        }

        playlistId?.let {
            playlistFragmentViewModel.refreshPlaylists()
        }

        binding.playlistFragmentRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.playlistFragmentRecyclerView.adapter = adapter

        val marginBetweenItems = resources.getDimensionPixelSize(R.dimen.top_margin)
        val marginToScreenEdges = resources.getDimensionPixelSize(R.dimen.padding_start)
        binding.playlistFragmentRecyclerView.addItemDecoration(
            MarginItemDecoration(marginBetweenItems, marginToScreenEdges)
        )

        // Подписка на изменения списка плейлистов
        playlistFragmentViewModel.allPlaylists.observe(viewLifecycleOwner) { playlists ->
            Log.d("PlaylistFragment", "Playlists received: ${playlists.size}")
            if (playlists.isEmpty()) {
                binding.playlistEmpty.visibility = View.VISIBLE
                binding.playlistNothing.visibility = View.VISIBLE
                binding.playlistFragmentRecyclerView.visibility = View.GONE
            } else {
                binding.playlistEmpty.visibility = View.GONE
                binding.playlistNothing.visibility = View.GONE
                binding.playlistFragmentRecyclerView.visibility = View.VISIBLE
                adapter.updatePlaylists(playlists)
            }
        }

        playlistFragmentViewModel.playlistWithTracks.observe(viewLifecycleOwner) { updatedPlaylist ->
            updatedPlaylist?.let {
                adapter.updateSinglePlaylist(it)
            }
        }

        updateBottomNavigationViewVisibility()
    }

    private fun openNewPlaylistFragment() {
        findNavController().navigate(R.id.action_playlistFragment_to_newPlaylistFragment)
    }

    private fun openPlaylist(playlist: NewPlaylistWithTracks) {
        val bundle = Bundle().apply {
            putString("playlistId", playlist.id)
        }
        findNavController().navigate(R.id.action_playlistFragment_to_openPlaylistFragment, bundle)
    }

    private fun updateBottomNavigationViewVisibility() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        updateBottomNavigationViewVisibility()

        playlistFragmentViewModel.refreshPlaylists()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}