package com.example.forecast.ui.favorites.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.forecast.R
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.data.repo.FavoriteCityRepo
import com.example.forecast.databinding.ActivityMapsBinding
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModel
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var dialogView: View
    private lateinit var buttonSave: Button
    private lateinit var alertDialog: AlertDialog
    private lateinit var textViewTitle: TextView
    private lateinit var binding: ActivityMapsBinding
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favoritesViewModelFactory =
            FavoritesViewModelFactory(FavoriteCityRepo.getInstance(application))
        favoritesViewModel =
            ViewModelProvider(this, favoritesViewModelFactory)[FavoritesViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        alertDialog = builder.create()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(23.0, 24.0)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        mMap.setOnMapClickListener { latLng ->
            val markerOptions = MarkerOptions()

            val geocoder = Geocoder(this, Locale.getDefault())

            val addresses: List<Address> = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude, 1
            ) as List<Address>

            val cityName: String = if (addresses[0].adminArea.isNullOrEmpty()) {
                getCityName(latLng.latitude, latLng.longitude)
            } else {
                addresses[0].adminArea
            }

            // Setting the position for the marker
            markerOptions.position(latLng)

            // Setting title for info window
            markerOptions.title("Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}")

            // Clears any existing markers from the Google Map
            googleMap.clear()

            // Placing a marker on touched position
            googleMap.addMarker(markerOptions)

            alertDialog.show()

            textViewTitle = dialogView.findViewById(R.id.textView_title)
            textViewTitle.text = "${latLng.latitude}"

            buttonSave = dialogView.findViewById(R.id.button_save)
            buttonSave.setOnClickListener {
                favoritesViewModel.viewModelScope.launch {
                    favoritesViewModel.insertCity(
                        FavoriteCity(
                            latLng.latitude,
                            latLng.longitude,
                            cityName
                        )
                    )
                }
            }
        }
    }

    private fun getCityName(lat: Double, long: Double): String {
        val cityName: String
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)

        cityName = address!![0].adminArea
        return cityName
    }
}