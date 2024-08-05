package com.example.myplaylist.library.ui.Playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myplaylist.R
import com.example.myplaylist.databinding.FragmentPlaylistsBinding
import com.example.myplaylist.library.data.NewPlaylist
import com.example.myplaylist.library.db.PlaylistEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistFragmentViewModel: PlaylistFragmentViewModel by inject()

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playlistFragmentViewModel.allPlaylists.collect { playlists ->
                    if (playlists.isEmpty()) {
                        binding.playlistEmpty.visibility = View.VISIBLE
                        binding.playlistNothing.visibility = View.VISIBLE
                        binding.playlistFragmentRecyclerView.visibility = View.GONE
                    } else {
                        binding.playlistEmpty.visibility = View.GONE
                        binding.playlistNothing.visibility = View.GONE
                        binding.playlistFragmentRecyclerView.visibility = View.VISIBLE
                        setupRecyclerView(playlists)
                    }
                }
            }
        }
        updateBottomNavigationViewVisibility()
    }

    private fun setupRecyclerView(playlists: List<NewPlaylist>) {
        binding.playlistFragmentRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.playlistFragmentRecyclerView.adapter = PlaylistFragmentAdapter(playlists)


        val marginBetweenItems = resources.getDimensionPixelSize(R.dimen.top_margin)
        val marginToScreenEdges = resources.getDimensionPixelSize(R.dimen.margin_size)
        val marginTopBetweenItems = resources.getDimensionPixelSize(R.dimen.margin_size)
        binding.playlistFragmentRecyclerView.addItemDecoration(
            MarginItemDecoration(marginBetweenItems, marginToScreenEdges, marginTopBetweenItems)
        )
    }

    private fun openNewPlaylistFragment() {
        findNavController().navigate(R.id.action_playlistFragment_to_newPlaylistFragment)
    }

    private fun updateBottomNavigationViewVisibility() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        updateBottomNavigationViewVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}