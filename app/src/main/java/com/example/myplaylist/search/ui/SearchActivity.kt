package com.example.myplaylist.search.ui

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplaylist.creator.Creator
import com.example.myplaylist.databinding.ActivitySearchBinding
import com.example.myplaylist.main.ui.App
import com.example.myplaylist.player.model.Track
import com.example.myplaylist.search.TrackAdapter
import com.example.myplaylist.search.domain.HistoryRepositoryImpl
import com.example.myplaylist.search.domain.SearchInteractor
import com.example.myplaylist.search.domain.SearchInteractorImpl
import com.example.myplaylist.search.domain.SearchRepositoryImpl
import com.example.myplaylist.search.domain.TrackDataSourceImpl
import com.example.myplaylist.player.ui.MediaPlayActivity
import kotlinx.coroutines.launch

const val TEXT_WATCHER = "TEXT_WATCHER"
const val SHARED_KEY_TRACK = "KEY_TRACK"
const val MAX_LIST_SIZE = 10
const val START_MEDIA_PUT_TRACK = "track"

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> {
        SearchViewModel.getViewModelFactory(
            getSharedPreferences(SHARED_KEY_TRACK, Context.MODE_PRIVATE),
            applicationContext,
            trackDataSource
        )
    }

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: TrackAdapter
    private lateinit var searchRepository: SearchRepositoryImpl
    private lateinit var searchInteractor: SearchInteractor
    private lateinit var trackDataSource: TrackDataSourceImpl
    private lateinit var historyRepositoryImpl: HistoryRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as App
        val itunesApi = app.provideItunesApi()
        val remoteTrackDataSourceImpl = Creator.getRemoteTrackDataSourceImpl(itunesApi)
        trackDataSource = TrackDataSourceImpl(remoteTrackDataSourceImpl)

        historyRepositoryImpl =
            HistoryRepositoryImpl(getSharedPreferences(SHARED_KEY_TRACK, Context.MODE_PRIVATE))

        val trackRepository = Creator.getTrackRepository(trackDataSource)
        searchRepository = SearchRepositoryImpl(trackRepository)

        searchInteractor =
            SearchInteractorImpl(searchRepository, historyRepositoryImpl, trackRepository)

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

        val networkUtils = Creator.getNetworkUtils(this)

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

        viewModel.getTracksLiveData().observe(this, { tracks ->
            binding.recyclerTrack.visibility = View.VISIBLE
            adapter.setData(tracks)
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

    private fun startMediaPlayerActivity(track: Track) {
        val intent = Intent(this, MediaPlayActivity::class.java)
        intent.putExtra(START_MEDIA_PUT_TRACK, track)
        startActivity(intent)
    }

}