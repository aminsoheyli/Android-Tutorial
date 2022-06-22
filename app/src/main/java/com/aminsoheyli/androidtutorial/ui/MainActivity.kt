package com.aminsoheyli.androidtutorial.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.utilities.Utility

private const val REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION = 123
//private const val REQUEST_CODE_ASK_PERMISSIONS_COARSE_LOCATION = 124

class MainActivity : AppCompatActivity() {
    private lateinit var buttonStorage: Button
    private lateinit var buttonGetLocation: Button
    private lateinit var textViewShowLocation: TextView
    private lateinit var lm: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initServices()
        initUi()
    }

    private fun initServices() {
        lm = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private fun initUi() {
        buttonStorage = findViewById(R.id.button_storage)
        buttonGetLocation = findViewById(R.id.button_get_location)
        textViewShowLocation = findViewById(R.id.textView_show_location)

        buttonStorage.setOnClickListener {
            startActivity(Intent(this, StorageActivity::class.java))
        }
        buttonGetLocation.setOnClickListener {
            showLocation()
        }
    }

    private fun showLocation() {
        // if LOCATION PERMISSION GRANTED
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
            checkSelfPermission(ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
        ) {
            val location =
                lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val message = "log: ${location?.longitude}, lat: ${location?.latitude}"
            textViewShowLocation.text = message
        } else if (!shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) &&
            !shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)
        )
            requestPermissions(
                arrayOf(
                    ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
                ), REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS_FINE_LOCATION ->
                if (grantResults[0] == PERMISSION_GRANTED || grantResults[1] == PERMISSION_GRANTED) {
                    showLocation()
                } else {
                    Utility.showSnackBar(buttonGetLocation, "You denied the location access")
                }
            else -> super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        }
    }
}