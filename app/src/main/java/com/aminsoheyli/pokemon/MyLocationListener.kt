package com.aminsoheyli.pokemon

import android.location.Location
import android.location.LocationListener

class MyLocationListener : LocationListener {
    companion object {
        var location = Location("start")
    }

    init {
        location.latitude = 0.0
        location.longitude = 0.0
    }

    override fun onLocationChanged(location: Location) {
        MyLocationListener.location = location
    }
}