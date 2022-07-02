package com.aminsoheyli.pokemon

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aminsoheyli.pokemon.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val REQUEST_CODE_ASK_LOCATION_PERMISSION = 1

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val listPokemon = arrayListOf<Pokemon>()
    private lateinit var oldLocation: Location
    private var myPower = 0.0
    private var isRefreshNeeded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        loadPokemon()
        runLocationChangeListener()
    }

    inner class MyThread : Thread() {
        init {
            oldLocation = Location("Start")
            oldLocation.latitude = 0.0
            oldLocation.longitude = 0.0
        }

        override fun run() {
            while (true) {
                sleep(1000)
                if (oldLocation.distanceTo(MyLocationListener.location) != 0f) {
                    oldLocation = MyLocationListener.location

                    runOnUiThread {
                        setLocation(MyLocationListener.location)
                        if (isRefreshNeeded) {
                            setLocation(MyLocationListener.location)
                            isRefreshNeeded = false
                        }
                    }
                }
            }
        }

        private fun setLocation(location: Location) {
            mMap.clear()
            val latLng = LatLng(
                location.latitude,
                location.longitude
            )
            mMap.addMarker(
                MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                    .title("Me, Power: $myPower")
            )
            if (!isRefreshNeeded)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
            for (i in 0 until listPokemon.size) {
                val pokemon = listPokemon[i]
                if (!pokemon.isCaught) {
                    addMarker(pokemon)
                    if (MyLocationListener.location.distanceTo(pokemon.location) < 5) {
                        myPower += pokemon.power
                        Toast.makeText(
                            this@MapsActivity,
                            "Catch pokemon, new power is: $myPower",
                            Toast.LENGTH_LONG
                        ).show()
                        pokemon.isCaught = true
                        isRefreshNeeded = true
                    }
                }
            }
        }

        private fun addMarker(pokemon: Pokemon) {
            val pokemonLocation = LatLng(
                pokemon.location.latitude,
                pokemon.location.longitude
            )
            mMap.addMarker(
                MarkerOptions().position(pokemonLocation)
                    .icon(BitmapDescriptorFactory.fromResource(pokemon.image))
                    .title(pokemon.name)
                    .snippet("${pokemon.description}, Power: ${pokemon.power}")
            )
        }
    }


    @SuppressLint("MissingPermission")
    private fun runLocationChangeListener() {
        if (isLocationPermissionGranted()) {
            val locationListener = MyLocationListener()
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                GPS_PROVIDER,
                3L,
                10f,
                locationListener
            )
            val thread = MyThread()
            thread.start()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
            return true
        else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) runLocationChangeListener()
                else Toast.makeText(this, "You denied location permission", Toast.LENGTH_LONG)
                    .show()
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    // Add list of pokemon
    private fun loadPokemon() {
        // -7.7572, 35.7067
        listPokemon.add(
            Pokemon(
                R.drawable.charmander,
                "Charmander",
                "Charmander living in japan",
                55.0,
                -7.7410, 35.7233
            )
        )
        listPokemon.add(
            Pokemon(
                R.drawable.bulbasaur,
                "Bulbasaur",
                "Bulbasaur living in usa",
                90.5,
                -7.7334, 35.7084
            )
        )
        listPokemon.add(
            Pokemon(
                R.drawable.squirtle,
                "Squirtle",
                "Squirtle living in iraq",
                33.5,
                -7.7609, 35.6871
            )
        )
    }
}