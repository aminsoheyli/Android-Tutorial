package com.aminsoheyli.paint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aminsoheyli.paint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        drawingView = binding.drawingView
        drawingView.setBrushSize(20f)
    }
}