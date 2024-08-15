package com.example.myplaylist.library.ui.Selected

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplaylist.databinding.FragmentSelectedBinding
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.player.ui.MediaPlayActivity
import com.example.myplaylist.search.ui.START_MEDIA_PUT_TRACK
import org.koin.android.ext.android.inject
class SelectedFragment : Fragment() {
    private var _binding: FragmentSelectedBinding? = null
    private val binding get() = _binding!!
    private val selectedFragmentViewModel: SelectedFragmentViewModel by inject()
    private var adapter: SelectedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SelectedAdapter { track ->
            handleTrackClick(track)
        }

        binding.recyclerSelected.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerSelected.adapter = adapter

        selectedFragmentViewModel.fillData()

        selectedFragmentViewModel.getSelectedFragmentLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: HistoryStateSelected) {
        when (state) {
            is HistoryStateSelected.Content -> showContent(state.trackSelected)
            is HistoryStateSelected.Empty -> showEmpty()
            is HistoryStateSelected.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.recyclerSelected.visibility = View.GONE
        binding.progressBarSelected.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.recyclerSelected.visibility = View.GONE
        binding.selectedEmpty.visibility = View.VISIBLE
        binding.selectedNothing.visibility = View.VISIBLE
        binding.progressBarSelected.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        binding.recyclerSelected.visibility = View.VISIBLE
        binding.selectedEmpty.visibility = View.GONE
        binding.selectedNothing.visibility = View.GONE
        binding.progressBarSelected.visibility = View.GONE
        val sortedTracks = tracks.sortedBy { it.addedTimestamp }.reversed()
        adapter?.tracks?.clear()
        adapter?.tracks?.addAll(sortedTracks)
        adapter?.notifyDataSetChanged()
    }

    private fun handleTrackClick(track: Track) {
        selectedFragmentViewModel.getTrackFavoriteStatus(track) { isFavorite ->
            Log.d("handleTrackClick", "track $track")
            startMediaPlayerActivity(track, isFavorite)
        }
    }

    private fun startMediaPlayerActivity(track: Track, isFavorite: Boolean) {
        val intent = Intent(requireContext(), MediaPlayActivity::class.java)
        intent.putExtra(START_MEDIA_PUT_TRACK, track)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
    override fun onResume() {
        super.onResume()
        selectedFragmentViewModel.fillData()
    }
}