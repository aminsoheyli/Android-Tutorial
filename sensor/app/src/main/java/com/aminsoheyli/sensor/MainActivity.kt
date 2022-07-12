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
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import kotlin.math.pow
import kotlin.math.sqrt


const val REQ_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
const val REQUEST_IMAGE_CAPTURE = 2

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var buttonTakePicture: Button
    private lateinit var buttonPlayAudio: Button
    private lateinit var imageView: ImageView
    private lateinit var lightSensor: Sensor
    private lateinit var acceleratorSensor: Sensor
    private lateinit var sensorManager: SensorManager
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlayButtonClicked = false
    private var isFinished = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
        initSensors()
    }

    private fun initSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        acceleratorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun initUi() {
        buttonTakePicture = findViewById(R.id.button_take_picture)
        buttonPlayAudio = findViewById(R.id.button_play_audio)
        imageView = findViewById(R.id.imageView)
        buttonTakePicture.setOnClickListener {
            takePicture()
        }
        buttonPlayAudio.setOnClickListener {
            playAudio()
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.alhamd)
    }

    private fun playAudio() {
        isPlayButtonClicked = true
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


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        if (!isFinished && mediaPlayer.isPlaying)
            mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, acceleratorSensor, SensorManager.SENSOR_DELAY_NORMAL)
        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show()
        if (!isFinished && !mediaPlayer.isPlaying)
            mediaPlayer.start()
    }

    private val preX = 0f
    private val preY = 0f
    private val preZ = 0f
    private val minSpeedToVibrate = 200
    private var preTime = 0L

    override fun onSensorChanged(event: SensorEvent?) {
        val sensor = event!!.sensor
        when (sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val timeDiff = System.currentTimeMillis() - preTime
                if (timeDiff > 100) {
                    val distance = sqrt(
                        (x - preX).toDouble().pow(2.0) + (y - preY).toDouble()
                            .pow(2.0) + (z - preZ).toDouble().pow(2.0)
                    )
                    val speed = distance / timeDiff * 1000
                    if (speed > minSpeedToVibrate) {
                        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(500)
                        Toast.makeText(this, "Shaked", Toast.LENGTH_SHORT).show()
                    }
                    preTime = System.currentTimeMillis()
                }
            }
            Sensor.TYPE_LIGHT -> {
                if (event.values[0] > 40) {
                    if (isFinished) {
                        if (isPlayButtonClicked)
                            try {
                                mediaPlayer.apply {
                                    setOnCompletionListener {
                                        this@MainActivity.isFinished = true
                                    }
                                    isLooping = false
                                    start()
                                }
                                isFinished = false
                                isPlayButtonClicked = false
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                    } else if (!mediaPlayer.isPlaying)
                        mediaPlayer.start()
                } else if (!isFinished && mediaPlayer.isPlaying)
                    mediaPlayer.pause()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}