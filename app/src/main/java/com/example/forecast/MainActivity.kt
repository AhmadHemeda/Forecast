package com.example.forecast

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.forecast.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSION_ID = 1010

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.weekFragment,
                R.id.favoritesFragment,
                R.id.notificationsFragment,
                R.id.settingsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermission()
        getLastLocation()
    }

    @SuppressLint("MissingPermission", "SetTextI18n", "UnlocalizedSms", "QueryPermissionsNeeded", "IntentReset")
    fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        newLocationData()
                    } else {
//                        locationTextView.text =
//                            "You Current Location is :" + "\n" +
//                                    "Long: " + location.longitude + "\n" +
//                                    "Lat: " + location.latitude + "\n" +
//                                    "City: " + getCityName(
//                                location.latitude,
//                                location.longitude
//                            )
//
//                        sendSMSButton.setOnClickListener {
//                            val phoneNumber = "01099180334"
//                            val address = getCityName(
//                                location.latitude,
//                                location.longitude
//                            )
//
//                            sendSMS(address, phoneNumber)
//                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission", "VisibleForTests")
    fun newLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location? = locationResult.lastLocation
//            locationTextView.text =
//                "You Last Location is : Long: " + lastLocation.longitude + "\n" + " , Lat: " + lastLocation.latitude + "\n" + getCityName(
//                    lastLocation.latitude,
//                    lastLocation.longitude
//                )
        }
    }

    private fun checkPermission(): Boolean {
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun requestPermission() {
        //this function will allows us to tell the user to request the necessary permission if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    private fun getCityName(lat: Double, long: Double): String {
        val cityName: String
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)

        cityName = address!![0].locality
        return cityName
    }

    private fun getCountryName(lat: Double, long: Double): String {
        val countryName: String
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)

        countryName = address!![0].countryName
        return countryName
    }
}