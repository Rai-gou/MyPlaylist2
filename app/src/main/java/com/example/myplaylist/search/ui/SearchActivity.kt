package com.example.myplaylist.search.ui

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplaylist.databinding.ActivitySearchBinding
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.player.ui.MediaPlayActivity
import org.koin.android.ext.android.inject

const val TEXT_WATCHER = "TEXT_WATCHER"
const val SHARED_KEY_TRACK = "KEY_TRACK"
const val MAX_LIST_SIZE = 10
const val START_MEDIA_PUT_TRACK = "track"

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.recyclerTrack.layoutManager = LinearLayoutManager(this@SearchActivity)
        binding.recyclerTrack.adapter = adapter

        observeViewModel()
        setupUI()
    }
    private fun observeViewModel() {
        viewModel.getLoadingLiveData.observe(this) { state ->
            binding.progressBar.isVisible = state.isLoading
            binding.recyclerTrack.isVisible = !state.isLoading && !state.isNothing && !state.isError
            binding.problemLayout.root.isVisible = state.isError
            binding.nothingLayout.root.isVisible = state.isNothing
            binding.clearIcon.isVisible = state.isClearIconVisible
            binding.buttonClearHistory.isVisible = state.isButtonClearHistoryVisible
            binding.history.isVisible = state.isYourHistoryVisible
            adapter.setData(state.tracks)
        }
    }

    private fun setupUI() {

        binding.inputEditText.apply {
            doBeforeTextChanged { _, _, _, _ ->
                // before
            }

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
                    binding.nothingLayout.root.visibility = View.GONE
                    binding.problemLayout.root.visibility = View.GONE
                    viewModel.loadHistoryTracks()
                }
            }
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
            binding.buttonClearHistory.visibility = View.GONE
            binding.history.visibility = View.GONE
        }

        binding.imageButtonSearch.setOnClickListener {
            finish()
        }

        binding.problemLayout.buttonProblem.setOnClickListener {
            viewModel.performSearch(binding.inputEditText.text?.toString()?.trim() ?: "")
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.text?.clear()
        }
        viewModel.loadHistoryTracks()
    }

    fun startMediaPlayerActivity(track: Track) {
        val intent = Intent(this, MediaPlayActivity::class.java)
        intent.putExtra(START_MEDIA_PUT_TRACK, track)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}