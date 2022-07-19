package com.aminsoheyli.paint

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
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

        findViewById<ImageView>(R.id.imageButton_brush_size).setOnClickListener {
            showBrushSizeChooserDialog()
        }
    }

    private fun showBrushSizeChooserDialog() {
        var brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush Size: ")
        val smallButton = brushDialog.findViewById<ImageView>(R.id.imageButton_small_brush)
        val mediumButton = brushDialog.findViewById<ImageView>(R.id.imageButton_medium_brush)
        val largeButton = brushDialog.findViewById<ImageView>(R.id.imageButton_large_brush)

        smallButton.setOnClickListener {
            drawingView.setBrushSize(10f)
            brushDialog.dismiss()
        }
        mediumButton.setOnClickListener {
            drawingView.setBrushSize(20f)
            brushDialog.dismiss()
        }
        largeButton.setOnClickListener {
            drawingView.setBrushSize(30f)
            brushDialog.dismiss()
        }
        brushDialog.show()
    }
}