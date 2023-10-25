package com.example.myplaylist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylist.databinding.ActivitySearchBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


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


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerTrack)
        val layoutManager =
            LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        clearButton.setOnClickListener {
            inputText.setText("")
            hideKeyboard()
            adapter.clearData()
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
                // empty
            }

        }

        inputText.addTextChangedListener(simpleTextWatcher)
        adapter = TrackAdapter()

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
            recyclerView.visibility = View.VISIBLE
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
        const val TEXT_WATCHER = "TEXT_WATCHER"
    }

}