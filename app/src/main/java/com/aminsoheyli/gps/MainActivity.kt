package com.aminsoheyli.gps

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val REQUEST_CODE_ASK_LOCATION_PERMISSION = 1

class MainActivity : AppCompatActivity() {
    private lateinit var textViewLocation: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewLocation = findViewById<TextView>(R.id.textView_location)
        val buttonShowLocation = findViewById<TextView>(R.id.button_show_location)
        buttonShowLocation.setOnClickListener {
            showLocation()
        }


    }

    private fun showLocation() {
        val location = getLastKnownLocation()
        if (location != null)
            textViewLocation.text = String.format(
                resources.getString(R.string.textView_show_location_text),
                location.longitude,
                location.latitude
            )
    }

    private fun getLastKnownLocation(): Location? {
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) == PERMISSION_GRANTED
        ) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_CODE_ASK_LOCATION_PERMISSION
            )
        return null
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
}