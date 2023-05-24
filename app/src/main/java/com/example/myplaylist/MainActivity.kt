package com.example.myplaylist

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonTwo = findViewById<Button>(R.id.mediaButton)
        buttonTwo.setOnClickListener {
            val intent = Intent(this, MediaSearchActivity::class.java)
            startActivity(intent)
        }

        val buttonOne = findViewById<Button>(R.id.searchButton)
        buttonOne.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)  }

        val buttonThree = findViewById<Button>(R.id.settingsButton)
        buttonThree.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)

        }

    }

}