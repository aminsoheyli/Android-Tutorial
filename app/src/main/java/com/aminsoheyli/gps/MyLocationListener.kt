package com.aminsoheyli.gps

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.Toast

class MyLocationListener(
    private val context: Context,
    private val locationUpdateInterface: MyLocationUpdateInterface
) : LocationListener {
    companion object {
        var location: Location? = null
    }

    override fun onLocationChanged(location: Location) {
        MyLocationListener.location = location
//        val message = String.format(
//            context.resources.getString(R.string.textView_show_location_text),
//            location.longitude,
//            location.latitude
//        )
//        locationUpdateInterface.onLocationUpdate(location)
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//        Toast.makeText(context, "GPS status changed to $status", Toast.LENGTH_LONG).show()
    }

    override fun onProviderEnabled(provider: String) {
//        Toast.makeText(context, "GPS is enabled", Toast.LENGTH_LONG).show()
    }

    override fun onProviderDisabled(provider: String) {
//        Toast.makeText(context, "GPS is disabled", Toast.LENGTH_LONG).show()
    }
}