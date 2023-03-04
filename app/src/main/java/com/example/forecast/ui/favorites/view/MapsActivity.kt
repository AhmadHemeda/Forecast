package com.example.forecast.ui.favorites.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.forecast.R
import com.example.forecast.data.model.FavoriteCity
import com.example.forecast.data.model.OpenWeatherResponse
import com.example.forecast.data.utils.Constants.Companion.LAT_LONG
import com.example.forecast.data.utils.Constants.Companion.MAPS_API_KEY
import com.example.forecast.databinding.ActivityMapsBinding
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModel
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.Arrays

private const val TAG = "MapsActivity"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var dialogView: View
    private lateinit var buttonSave: Button
    private lateinit var dialog: AlertDialog
    private lateinit var textViewTitle: TextView
    private lateinit var binding: ActivityMapsBinding
    private lateinit var favoritesViewModel: FavoritesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favoritesViewModelFactory = FavoritesViewModelFactory(this)
        favoritesViewModel =
            ViewModelProvider(this, favoritesViewModelFactory)[FavoritesViewModel::class.java]

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        dialog = builder.create()
/*

response = intent.getSerializableExtra(LAT_LONG) as OpenWeatherResponse

if (!Places.isInitialized()) {
Places.initialize(applicationContext, MAPS_API_KEY)
}

val autocompleteSupportFragment =
supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

autocompleteSupportFragment.setLocationBias(
RectangularBounds.newInstance(
LatLng(response.lat, response.lon),
LatLng(response.lat, response.lon)
)
)

autocompleteSupportFragment.setPlaceFields(
listOf(
Place.Field.ID,
Place.Field.NAME,
Place.Field.LAT_LNG
)
)
*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val latLng = LatLng(23.0, 24.0)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        // Set a listener for map click.
        mMap.setOnMapClickListener { latLng ->
            // Creating a marker
            val markerOptions = MarkerOptions()

            // Setting the position for the marker
            markerOptions.position(latLng)

            // Setting title for info window
            markerOptions.title("Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}")

            // Clears any existing markers from the Google Map
            googleMap.clear()

            // Placing a marker on touched position
            googleMap.addMarker(markerOptions)

            dialog.show()

            textViewTitle = dialogView.findViewById(R.id.textView_title)
            textViewTitle.text = "${latLng.latitude}"

            buttonSave = dialogView.findViewById(R.id.button_save)
            buttonSave.setOnClickListener {
                favoritesViewModel.viewModelScope.launch {
                    favoritesViewModel.insertCity(
                        FavoriteCity(
                            latLng.latitude,
                            latLng.longitude,
                            ""
                        )
                    )
                }
            }

        }
    }
}

//private fun showAlertDialog() {
//
//    MaterialAlertDialogBuilder(this)
//        .setTitle("Alert")
//        .setMessage("Do you want to add this city to your favorites?")
//        .setNegativeButton("No") { _, _ ->
//            Toast.makeText(this, "Back to map", Toast.LENGTH_SHORT).show()
//        }
//        .setPositiveButton("Yes") { _, _ ->
//            Toast.makeText(this, "Go to favorites", Toast.LENGTH_SHORT).show()
//        }
//}