package com.aminsoheyli.sensor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private lateinit var buttonTakePicture: Button
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        buttonTakePicture = findViewById(R.id.button_take_picture)
        imageView = findViewById(R.id.imageView)
        buttonTakePicture.setOnClickListener {
        }
    }
}