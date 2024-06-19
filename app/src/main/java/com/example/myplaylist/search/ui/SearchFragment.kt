package com.example.myplaylist.search.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplaylist.R
import com.example.myplaylist.databinding.FragmentSearchBinding
import com.example.myplaylist.main.ui.RootActivity
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.player.ui.MediaPlayActivity
import org.koin.android.ext.android.inject

const val TEXT_WATCHER = "TEXT_WATCHER"
const val SHARED_KEY_TRACK = "KEY_TRACK"
const val MAX_LIST_SIZE = 10
const val START_MEDIA_PUT_TRACK = "track"

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by inject()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? RootActivity)?.registerSearchFragment(this)

        adapter = TrackAdapter(this) { track ->
            viewModel.onItemClick(track) { trackSaved ->
                Log.d("MyLog", "trackSaved: $trackSaved")
                if (trackSaved) {
                    startMediaPlayerActivity(track)
                } else {
                    Log.d("MyLog", "trackSaved: error")
                }
            }
        }

        binding.recyclerTrack.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerTrack.adapter = adapter

        observeViewModel()
        setupUI()
    }

    private fun observeViewModel() {
        viewModel.getLoadingLiveData.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state.isLoading
            binding.recyclerTrack.isVisible = !state.isLoading && !state.isNothing && !state.isError
            if (state.isError) {
                handleProblemState()
            } else {
                binding.problemFragment.visibility = View.GONE
            }
            if (state.isNothing) {
                handleNothingState()
            } else {
                binding.nothingFragment.visibility = View.GONE
            }
            binding.problemFragment.isVisible = state.isError
            binding.clearIcon.isVisible = state.isClearIconVisible
            binding.buttonClearHistory.isVisible = state.isButtonClearHistoryVisible
            binding.history.isVisible = state.isYourHistoryVisible
            adapter.setData(state.tracks)
        }
    }

    private fun setupUI() {

        binding.inputEditText.apply {
            doBeforeTextChanged { _, _, _, _ -> }

            doOnTextChanged { text, _, _, _ ->
                val query = text?.toString()?.trim() ?: ""
                viewModel.performSearch(query)
            }

            doAfterTextChanged { editable ->
                Log.d("MyLog", "emptyText: $editable")
                if (editable.isNullOrEmpty()) {
                    if (viewModel.searchJob?.isActive == true) {
                        viewModel.searchJob?.cancel()
                    }
                    viewModel.loadHistoryTracks()
                }
            }
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
            binding.buttonClearHistory.visibility = View.GONE
            binding.history.visibility = View.GONE
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.text?.clear()
        }
        viewModel.loadHistoryTracks()
    }

    fun performSearchWithCurrentText() {
        val query = binding.inputEditText.text?.toString()?.trim() ?: ""
        viewModel.performSearch(query)
    }

    private fun handleNothingState() {
        val fragmentManager = childFragmentManager
        fragmentManager.commit {
            replace(R.id.nothingFragment, NothingFragment())
            addToBackStack(null)
        }
        binding.nothingFragment.visibility = View.VISIBLE
    }

    private fun handleProblemState() {
        val query = binding.inputEditText.text.toString().trim()
        val fragmentManager = childFragmentManager
        val problemFragment = ProblemFragment.newInstance(query)
        fragmentManager.commit {
            replace(R.id.problemFragment, problemFragment)
            addToBackStack(null)
        }
        binding.problemFragment.visibility = View.VISIBLE
    }

    private fun startMediaPlayerActivity(track: Track) {
        val intent = Intent(requireContext(), MediaPlayActivity::class.java)
        intent.putExtra(START_MEDIA_PUT_TRACK, track)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}