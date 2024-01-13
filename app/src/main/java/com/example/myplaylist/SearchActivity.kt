package com.example.myplaylist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.databinding.ActivitySearchBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TEXT_WATCHER = "TEXT_WATCHER"


class SearchActivity : AppCompatActivity(), OnItemClickListener {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val itunesBaseUrl = "https://itunes.apple.com"
    private lateinit var adapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var inputText: EditText
    private var searchQuery: String = ""
    private var updateList = TrackAdapter(this)
    val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val itunesApi = retrofit.create(ItunesApi::class.java)
    private lateinit var sharedPreferencesTrack: SharedPreferences
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


        adapter = TrackAdapter(this)
        sharedPreferencesTrack = getSharedPreferences(SHARED_KEY_TRACK, Context.MODE_PRIVATE)

        val buttonHistory = findViewById<Button>(R.id.buttonHistory)
        val textHistory = findViewById<View>(R.id.history)
        val problemLayout = findViewById<View>(R.id.problemLayout)
        val nothingLayout = findViewById<View>(R.id.nothingLayout)

        buttonHistory.setOnClickListener {
            textHistory.visibility = View.GONE
            buttonHistory.visibility = View.GONE
            adapter.clearHistory()
            sharedPreferencesTrack.edit().clear().apply()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTrack)

        searchHistory = SearchHistory(
            sharedPreferencesTrack,
            adapter
        )

        clearButton.setOnClickListener {
            inputText.setText("")
            hideKeyboard()
            adapter.clearData()

        }

        loadList()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                var historyListSize = (adapter as? TrackAdapter)?.getHistoryList()?.size
                Log.d("MyLog", "afterTextChangedhistoryListSize: $historyListSize")
                if (historyListSize != 0) {
                    adapter.getHistoryList()
                    recyclerView.visibility =
                        if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
                    textHistory.visibility =
                        if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
                    buttonHistory.visibility =
                        if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
                }
                problemLayout.visibility =
                    if (inputText.text.isEmpty()) View.GONE else problemLayout.visibility
                nothingLayout.visibility =
                    if (inputText.text.isEmpty()) View.GONE else nothingLayout.visibility

            }

        }

        adapter.setOnItemClickListener(this)
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

    private fun loadList() {
        val buttonHistory = findViewById<Button>(R.id.buttonHistory)
        val textHistory = findViewById<View>(R.id.history)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTrack)
        val loadHistory = searchHistory.loadHistoryList()
        adapter.updateHistoryList(loadHistory)
        Log.d("MyLog", "loadList: ${loadHistory.size}")
        if (loadHistory.size != 0) {
            recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            recyclerView.adapter = adapter
            adapter.getHistoryList()
            recyclerView.visibility =
                if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
            textHistory.visibility =
                if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
            buttonHistory.visibility =
                if (inputText.text.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    fun saveSelectedViewPosition(sharedPreferences: SharedPreferences, position: Int) {
        sharedPreferences.edit {
            putInt(SHARED_KEY_TRACK, position)
            apply()
        }
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

    override fun onItemClick(track: Int) {
        val clickedItem = adapter.getItem(track)
        Log.d("MyLog", "clickedItem: $clickedItem")
        searchHistory.saveHistoryTrack(clickedItem)
    }


}