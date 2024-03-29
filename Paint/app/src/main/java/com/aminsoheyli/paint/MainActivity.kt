package com.aminsoheyli.paint

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.aminsoheyli.paint.databinding.ActivityMainBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    companion object {
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY_CODE = 2
    }

    private lateinit var drawingView: DrawingView
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageButtonCurrentPaint: ImageButton
    private var progressDialog: ProgressDialog? = null
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

        binding.imageButtonGallery.setOnClickListener {
            if (isReadStoragePermissionGranted()) {
                pickBackgroundImage()
            } else {
                requestStoragePermission()
            }
        }
        binding.imageButtonUndo.setOnClickListener { drawingView.undo() }
        binding.imageButtonRedo.setOnClickListener { drawingView.redo() }
        binding.imageButtonSaveImage.setOnClickListener {
            if (isWriteStoragePermissionGranted()) {
                showProgressDialog()
                lifecycleScope.launch {
                    val frameLayoutDrawingView = binding.frameLayoutDrawingViewContainer
                    saveBitmapFile(getBitmapFromView(frameLayoutDrawingView))
                }
            } else {
                requestStoragePermission()
            }
        }
        binding.imageButtonBrushColor.setBackgroundColor(drawingView.getColor())
        binding.imageButtonBrushColor.setOnClickListener { showColorPickerDialog() }
    }

    private fun showColorPickerDialog() {
        val colorPickerDialogBuilder = ColorPickerDialog.Builder(this)
            .setTitle("Pick a color")
            .setPositiveButton("SELECT",
                ColorEnvelopeListener { envelope, fromUser ->
                    drawingView.setColor("#${envelope.hexCode}")
                    binding.imageButtonBrushColor.setBackgroundColor(envelope.color)
                })
            .setNegativeButton("CANCEL") { dialogInterface, i -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true) // the default value is true.
            .attachBrightnessSlideBar(true) // the default value is true.
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
            .setView(R.layout.dialog_brush_color)
        val bubbleFlag = BubbleFlag(this)
        bubbleFlag.flagMode = FlagMode.FADE
        colorPickerDialogBuilder.colorPickerView.flagView = bubbleFlag
        colorPickerDialogBuilder.show()
    }

    private fun pickBackgroundImage() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, GALLERY_CODE)
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

    private fun requestStoragePermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), STORAGE_PERMISSION_CODE
            )
        else
            Toast.makeText(this, "You just denied the permission.", Toast.LENGTH_SHORT).show()
    }

    private fun isReadStoragePermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun isWriteStoragePermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Permission granted now you car read the storage files.",
                        Toast.LENGTH_SHORT
                    ).show()
                    pickBackgroundImage()
                } else
                    Toast.makeText(this, "You just denied the permission.", Toast.LENGTH_SHORT)
                        .show()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val backgroundDrawable = view.background
        if (backgroundDrawable != null)
            backgroundDrawable.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }

    private suspend fun saveBitmapFile(bitmap: Bitmap?): String {
        var result = ""
        withContext(Dispatchers.IO) {
            if (bitmap != null) {
                try {
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
                    val file =
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + File.separator + "Paint_" + System.currentTimeMillis() / 1000 + ".png")
                    val fo = FileOutputStream(file)
                    fo.write(bytes.toByteArray())
                    fo.close()
                    result = file.absolutePath
                    runOnUiThread {
                        cancelProgressDialog()
                        if (result.isNotEmpty()) {
                            Toast.makeText(
                                this@MainActivity, result,
                                Toast.LENGTH_LONG
                            ).show()
                            shareImage(result)
                        } else
                            Toast.makeText(
                                this@MainActivity, "Something went wrong while saving the file.",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                } catch (e: Exception) {
                    result = ""
                    cancelProgressDialog()
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    private fun showProgressDialog() {
        @Suppress("DEPRECATION")
        progressDialog = ProgressDialog.show(
            this,
            "",
            "Saving your image..."
        )
    }

    private fun cancelProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    private fun shareImage(imagePath: String) {
        MediaScannerConnection.scanFile(applicationContext, arrayOf(imagePath), null) { path, uri ->
            val shareIntent =
                Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_STREAM, uri).setType("image/png")
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == GALLERY_CODE) {
                try {
                    if (data!!.data != null) {
                        binding.imageViewBackground.apply {
                            visibility = View.VISIBLE
                            setImageURI(data.data)
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Error in parsing the image or its corrupted.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }
}