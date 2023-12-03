package com.example.myplaylist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.databinding.ActivitySearchBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.lifecycle.ViewModel


class SearchActivity : AppCompatActivity() {

    private val itunesBaseUrl = "https://itunes.apple.com"
    private lateinit var adapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var inputText: EditText
    private var searchQuery: String = ""
    private var updateList = TrackAdapter()
    val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val itunesApi = retrofit.create(ItunesApi::class.java)
    private lateinit var sharedPreferencesTrack: SharedPreferences
    private var selectedViewPosition: Int = -1
    private lateinit var searchHistory: SearchHistory

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBack = findViewById<ImageButton>(R.id.imageButtonSearch)
        actionBack.setOnClickListener {
            finish()
        }



        inputText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(TEXT_WATCHER)
            inputText.setText(savedText)
        }
        inputText.isFocusableInTouchMode
        inputText.isFocusable = true
        inputText.requestFocus()


        adapter = TrackAdapter()
        sharedPreferencesTrack = getSharedPreferences(SHARED_KEY_TRACK, Context.MODE_PRIVATE)

        val buttonHistory = findViewById<Button>(R.id.buttonHistory)
        val textHistory = findViewById<View>(R.id.history)
        val problemLayout = findViewById<View>(R.id.problemLayout)
        val nothingLayout = findViewById<View>(R.id.nothingLayout)

        buttonHistory.setOnClickListener {
            textHistory.visibility = View.GONE
            buttonHistory.visibility = View.GONE
            adapter.clearHistory()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTrack)

        searchHistory = SearchHistory(
            sharedPreferencesTrack,
            adapter
        )

        val onItemClick: (Track) -> Unit = { track ->
            Log.d("MyLog", "click: $track")
        }

        recyclerView.addOnItemClickListener(searchHistory, onItemClick)




        clearButton.setOnClickListener {
            inputText.setText("")
            hideKeyboard()
            adapter.clearData()
            showHistory()

        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                showHistory()
                var historyListSize = (adapter as? TrackAdapter)?.getHistoryList()?.size
                Log.d("MyLog", "historyListSize: $historyListSize")
                if (historyListSize != 0) {
                    recyclerView.visibility =
                        if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
                    textHistory.visibility =
                        if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
                    buttonHistory.visibility =
                        if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
                }
                problemLayout.visibility = if (inputText.text.isEmpty()) View.GONE else problemLayout.visibility
                nothingLayout.visibility = if (inputText.text.isEmpty()) View.GONE else nothingLayout.visibility
            }

        }

        inputText.addTextChangedListener(simpleTextWatcher)

        inputText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                init()
                true
            } else {
                false
            }
        }
    }

    private fun init() {
        val problemLayout = findViewById<View>(R.id.problemLayout)
        val nothingLayout = findViewById<View>(R.id.nothingLayout)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTrack)

        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        recyclerView.adapter = adapter

        if (isNetworkAvailable()) {
            adapter.clearData()
            problemLayout.visibility = View.GONE
            nothingLayout.visibility = View.GONE

            val encodedQuery = "\"$searchQuery\""
            itunesApi.search(encodedQuery).enqueue(object : Callback<ResponseClass> {
                override fun onResponse(
                    call: Call<ResponseClass>,
                    response: Response<ResponseClass>
                ) {
                    if (response.isSuccessful) {
                        val results = response.body()
                        if (results != null) {
                            val trackList = results.results
                            if (trackList.isNotEmpty()) {
                                adapter.updateList(trackList)
                                recyclerView.visibility = View.VISIBLE
                            } else {
                                nothingLayout.visibility = View.VISIBLE
                                adapter.clearData()
                            }
                        }
                    } else {
                        problemLayout.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<ResponseClass>, t: Throwable) {
                    problemLayout.visibility = View.VISIBLE
                }
            })
        } else {
            recyclerView.visibility = View.GONE
            nothingLayout.visibility = View.GONE
            problemLayout.visibility = View.VISIBLE
            val buttonProblem = findViewById<Button>(R.id.buttonProblem)
            buttonProblem.setOnClickListener {
                if (isNetworkAvailable()) {
                    problemLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                } else {
                    problemLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    fun RecyclerView.addOnItemClickListener(
        searchHistory: SearchHistory,
        onItemClick: (Track) -> Unit
    ) {
        this.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(
                recyclerView: RecyclerView,
                event: MotionEvent
            ): Boolean {
                if (event.action == MotionEvent.ACTION_UP) {
                    val addItem = recyclerView.findChildViewUnder(event.x, event.y)
                    Log.d("MyLog", "addItem: $addItem")
                    if (addItem != null) {
                        val position = recyclerView.getChildAdapterPosition(addItem)
                        Log.d("MyLog", "position: $position")
                        if (position < (adapter as? TrackAdapter)?.getTrackList()?.size ?: 0) {
                            val track = (adapter as? TrackAdapter)?.getTrackList()?.get(position)
                            Log.d("MyLog", "track: $track")
                            if (track != null) {
                                onItemClick(track)
                                searchHistory.saveHistoryTrack(track)
                            } else {
                                Log.d("MyLog", "Invalid position: $position")
                            }
                        } else {
                            Log.d("MyLog", "Invalid position: $position")
                        }
                    }
                }
                return false
            }
        })
    }

    fun saveSelectedViewPosition(sharedPreferences: SharedPreferences, position: Int) {
        sharedPreferences.edit {
            putInt(SHARED_KEY_TRACK, position)
            apply()
        }
        Log.d("MyLog", "saveSelectedViewPosition: $position")
    }

    fun showSelectedItemContents(adapter: TrackAdapter, sharedPreferences: SharedPreferences) {
        val selectedPosition = sharedPreferences.getInt(SHARED_KEY_TRACK, -1)
        val trackSaveList =
            adapter.getHistoryList()

        if (selectedPosition != -1 && selectedPosition < trackSaveList.size) {
            val selectedItem = trackSaveList[selectedPosition]
            Log.d("MyLog", "Selected Track: $selectedItem")
        } else {
            Log.d("MyLog", "Invalid selected position")
        }
    }

    fun showHistory() {

        adapter.clearData()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTrack)
        val historyList = adapter.historyList

        val layoutManager =
            LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.updateHistory(historyList)
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    @OptIn(DelicateCoroutinesApi::class)

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val textToSave = inputText.text.toString()
        outState.putString(TEXT_WATCHER, textToSave)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(TEXT_WATCHER)
        inputText.setText(savedText)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchActivity()
        const val TEXT_WATCHER = "TEXT_WATCHER"
    }

}