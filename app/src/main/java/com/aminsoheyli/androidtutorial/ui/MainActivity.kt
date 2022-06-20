package com.aminsoheyli.androidtutorial.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.androidtutorial.R

class MainActivity : AppCompatActivity() {
    private lateinit var buttonStorage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        buttonStorage = findViewById(R.id.button_storage)
        buttonStorage.setOnClickListener {
            startActivity(Intent(this, StorageActivity::class.java))
        }
    }
}