package com.aminsoheyli.androidtutorial.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.androidtutorial.R

class CounterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
    }
}