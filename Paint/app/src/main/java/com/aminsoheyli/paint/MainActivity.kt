package com.aminsoheyli.paint

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.iterator
import com.aminsoheyli.paint.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageButtonCurrentPaint: ImageButton
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
        // Paint Color
        imageButtonCurrentPaint = linearLayout_paint_colors[0] as ImageButton
        imageButtonCurrentPaint.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )
        for (view in linearLayout_paint_colors.iterator())
            view.setOnClickListener { paintColorClicked(view) }
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

    private fun paintColorClicked(view: View) {
        if (view !== imageButtonCurrentPaint) {
            val imageButton = view as ImageButton
            imageButtonCurrentPaint.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_normal)
            )
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
            )
            val colorTag = imageButton.tag.toString()
            drawingView.setColor(colorTag)
            imageButtonCurrentPaint = imageButton
        }
    }
}