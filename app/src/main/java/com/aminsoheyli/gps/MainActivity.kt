package com.aminsoheyli.gps

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val REQUEST_CODE_ASK_LOCATION_PERMISSION = 1

class MainActivity : AppCompatActivity(), MyLocationUpdateInterface {
    private var isLocationShown = false
    private lateinit var textViewLocation: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewLocation = findViewById<TextView>(R.id.textView_location)
        val buttonShowLocation = findViewById<TextView>(R.id.button_show_location)
        buttonShowLocation.setOnClickListener {
            showLocation()
        }

        if (isLocationPermissionGranted()) {

        }
    }

    @SuppressLint("MissingPermission")
    private fun showLocation() {
        if (isLocationPermissionGranted()) {
            if (!isLocationShown) {
                // Show last known location
                val location = getLastKnownLocation()
                if (location != null)
                    textViewLocation.text = String.format(
                        resources.getString(R.string.textView_show_location_text),
                        location.longitude,
                        location.latitude
                    )
                isLocationShown = true
            }
            // Show current location with minTimeMs=3000 and minDistanceM=10
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val locationListener = MyLocationListener(this, this)
            locationManager.requestLocationUpdates(GPS_PROVIDER, 3000L, 10f, locationListener)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.getLastKnownLocation(GPS_PROVIDER)
    }

    private fun isLocationPermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) == PERMISSION_GRANTED
        )
            return true
        else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_CODE_ASK_LOCATION_PERMISSION
            )
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_LOCATION_PERMISSION ->
                if (grantResults[0] == PERMISSION_GRANTED) showLocation()
                else Toast.makeText(this, "You denied location permission", Toast.LENGTH_LONG)
                    .show()
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

    override fun onLocationUpdate(location: Location) {
        textViewLocation.text = String.format(
            resources.getString(R.string.textView_show_location_text),
            location.longitude,
            location.latitude
        )
    }

}

