package com.aminsoheyli.androidtutorial.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.utilities.Utility

private const val REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION = 123
//private const val REQUEST_CODE_ASK_PERMISSIONS_COARSE_LOCATION = 124

class MainActivity : AppCompatActivity() {
    private lateinit var buttonStorage: Button
    private lateinit var buttonGetLocation: Button
    private lateinit var textViewShowLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        buttonStorage = findViewById(R.id.button_storage)
        buttonGetLocation = findViewById(R.id.button_get_location)
        textViewShowLocation = findViewById(R.id.textView_show_location)

        buttonStorage.setOnClickListener {
            startActivity(Intent(this, StorageActivity::class.java))
        }
        buttonGetLocation.setOnClickListener {
            checkUserPermissions()
//            textViewShowLocation.text = getLocation()
        }
    }

    private fun checkUserPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION
                )
            return
        }
        getLocation()
    }

    // TODO: When user grant APPROXIMATE Location
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    // Permission Denied
                    Utility.showSnackBar(buttonGetLocation, "You denied the location access")
                }
            else -> super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        }
    }

    //    private fun getLocation(): String {
    private fun getLocation() {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val message = "log: ${location?.longitude}, lat: ${location?.latitude}"
        textViewShowLocation.text = message
    }
}