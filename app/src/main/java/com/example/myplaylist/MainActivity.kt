package com.example.myplaylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonTwo = findViewById<Button>(R.id.ButtonMaintwo)
        buttonTwo.setOnClickListener {
            val intent = Intent(this, mediasherch::class.java)
            startActivity(intent)
        }

        val buttonOne = findViewById<Button>(R.id.Buttonmainone)
        buttonOne.setOnClickListener{
            val intent = Intent(this, search::class.java)
            startActivity(intent)  }

        val buttonThree = findViewById<Button>(R.id.ButtonMainthree)
        buttonThree.setOnClickListener {
            val intent = Intent(this, settings::class.java)
            startActivity(intent)

        }

    }

}