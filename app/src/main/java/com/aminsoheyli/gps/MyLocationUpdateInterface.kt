package com.aminsoheyli.gps

import android.location.Location

interface MyLocationUpdateInterface {
    fun onLocationUpdate(location: Location)
}