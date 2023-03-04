package com.example.forecast.ui.favorites.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.forecast.R
import com.example.forecast.data.model.OpenWeatherResponse
import com.example.forecast.data.utils.Constants.Companion.LAT_LONG
import com.example.forecast.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val TAG = "MapsActivity"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var response: OpenWeatherResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        response = intent.getSerializableExtra(LAT_LONG) as OpenWeatherResponse

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.i(TAG, "onMapReady: ${response.lat}")

        // Add a marker in Sydney and move the camera
        val latLng = LatLng(response.lat, response.lon)
        mMap.addMarker(MarkerOptions().position(latLng).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }
}