package com.aminsoheyli.sensor

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.IOException


const val REQ_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
const val REQUEST_IMAGE_CAPTURE = 2

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var buttonTakePicture: Button
    private lateinit var imageView: ImageView
    private lateinit var sensor: Sensor
    private lateinit var sensorManager: SensorManager
    private lateinit var mediaPlayer: MediaPlayer
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
        initSensors()
    }

    private fun initSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    }

    private fun initUi() {
        buttonTakePicture = findViewById(R.id.button_take_picture)
        imageView = findViewById(R.id.imageView)
        buttonTakePicture.setOnClickListener {
            takePicture()
        }
        mediaPlayer = MediaPlayer()
    }

    private fun takePicture() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
            }
        } else
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQ_PERMISSION_WRITE_EXTERNAL_STORAGE
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> if (resultCode == RESULT_OK) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(imageBitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQ_PERMISSION_WRITE_EXTERNAL_STORAGE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) takePicture()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show()
        if (isRunning)
            mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        if (isRunning)
            mediaPlayer.pause()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.values[0] > 40)
            if (!isRunning)
                try {
                    mediaPlayer.setDataSource("https://cdn6.iribtv.ir/9/original/2018/07/16/636673403410852046.mp3")
                    mediaPlayer.isLooping = false
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    isRunning = true
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            else
                mediaPlayer.start()
        else {
            mediaPlayer.pause()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}