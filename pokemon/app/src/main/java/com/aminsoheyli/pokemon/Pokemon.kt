package com.aminsoheyli.pokemon

import android.location.Location

class Pokemon(
    var image: Int,
    var name: String,
    var description: String,
    val power: Double,
    lat: Double,
    long: Double
) {
    val location: Location = Location(name)
    var isCaught = false

    init {
        location.latitude = lat
        location.longitude = long
    }
}