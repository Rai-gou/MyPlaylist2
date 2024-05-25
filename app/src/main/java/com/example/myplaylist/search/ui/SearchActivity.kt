package com.example.myplaylist.search.ui

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplaylist.databinding.ActivitySearchBinding
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.domain.SearchInteractor
import com.example.myplaylist.search.domain.SearchRepositoryImpl
import com.example.myplaylist.player.ui.MediaPlayActivity
import com.example.myplaylist.search.data.HistoryRepository
import com.example.myplaylist.search.data.NetworkUtils
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

const val TEXT_WATCHER = "TEXT_WATCHER"
const val SHARED_KEY_TRACK = "KEY_TRACK"
const val MAX_LIST_SIZE = 10
const val START_MEDIA_PUT_TRACK = "track"

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: TrackAdapter
    private val searchInteractor: SearchInteractor by inject()
    private val networkUtils: NetworkUtils by inject()
    private val searchRepository: SearchRepositoryImpl by inject()
    private val historyRepositoryImpl: HistoryRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TrackAdapter(this) { track ->
            lifecycleScope.launch {
                searchInteractor.onItemClick(track) { trackSaved ->
                    Log.d("MyLog", "trackSaved: $trackSaved")
                    if (trackSaved) {
                        historyRepositoryImpl.saveHistoryTrack(track)
                    } else {
                        Log.d("MyLog", "trackSaved: error")
                    }
                    startMediaPlayerActivity(track)
                }
            }
        }

        binding.recyclerTrack.layoutManager = LinearLayoutManager(this@SearchActivity)
        binding.recyclerTrack.adapter = adapter

        observeViewModel()
        setupUI()
    }

    private fun observeViewModel() {
        viewModel.progressBarVisible().observe(this, { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.recyclerViewVisible().observe(this, { isLoading ->
            binding.recyclerTrack.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.errorActivityVisible().observe(this, { isLoading ->
            binding.problemLayout.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.nothingActivityVisible().observe(this, { isLoading ->
            binding.nothingLayout.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.clearIcon().observe(this, { isLoading ->
            binding.clearIcon.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.buttonClearHistory().observe(this, { isLoading ->
            binding.buttonClearHistory.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.yourHistory().observe(this, { isVisible ->
            binding.history.visibility = if (isVisible) View.VISIBLE else View.GONE
        })

        viewModel.getTracksLiveData().observe(this, { tracks ->
            binding.recyclerTrack.visibility = View.VISIBLE
            adapter.setData(tracks)
        })
    }
    private fun setupUI() {

        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Не используется
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim() ?: ""
                viewModel.performSearch(query, networkUtils)
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("MyLog", "emptyText: $s")
                if (s.isNullOrEmpty()) {
                    if (viewModel.searchJob?.isActive == true) {
                        viewModel.searchJob?.cancel()
                    }
                    binding.nothingLayout.root.visibility = View.GONE
                    binding.problemLayout.root.visibility = View.GONE
                    viewModel.loadHistoryTracks()

                }
            }
        })

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
            binding.buttonClearHistory.visibility = View.GONE
            binding.history.visibility = View.GONE
        }

        binding.imageButtonSearch.setOnClickListener {
            finish()
        }

        binding.problemLayout.buttonProblem.setOnClickListener {
            if (networkUtils.isNetworkAvailable()) {
                viewModel.performSearch(
                    binding.inputEditText.text?.toString()?.trim() ?: "",
                    networkUtils
                )
                binding.problemLayout.root.visibility = View.GONE
                binding.recyclerTrack.visibility = View.VISIBLE
            }
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.text?.clear()
            binding.clearIcon.visibility = View.GONE

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