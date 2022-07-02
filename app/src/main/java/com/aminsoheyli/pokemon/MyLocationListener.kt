package com.aminsoheyli.pokemon

import android.location.Location
import android.location.LocationListener

class MyLocationListener : LocationListener {
    companion object {
        var location: Location? = null
    }

    override fun onLocationChanged(location: Location) {
        MyLocationListener.location = location
    }
}