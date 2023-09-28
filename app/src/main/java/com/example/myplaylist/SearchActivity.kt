package com.example.myplaylist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment

class SearchActivity : AppCompatActivity() {

    private lateinit var inputText: EditText
    private var searchQuery: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
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
        clearButton.setOnClickListener {
            inputText.setText("")
            hideKeyboard()
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
    }

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
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
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

