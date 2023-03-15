package com.example.forecast.ui.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.forecast.R
import com.example.forecast.data.utils.Constants
import com.example.forecast.databinding.ActivityHomeMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class HomeMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityHomeMapBinding
    private lateinit var dialogView: View
    private lateinit var alertDialog: AlertDialog
    private lateinit var textViewTitle: TextView
    private lateinit var buttonSave: Button
//    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        homeViewModel =
//            ViewModelProvider(this)[HomeViewModel::class.java]

        dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        alertDialog = builder.create()
    }

    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(23.0, 24.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        mMap.setOnMapClickListener { latLng ->
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}")
            googleMap.clear()
            googleMap.addMarker(markerOptions)

            alertDialog.show()

            textViewTitle = dialogView.findViewById(R.id.textView_title)
            textViewTitle.text = "Change your location to ${latLng.latitude}"

            buttonSave = dialogView.findViewById(R.id.button_save)
            buttonSave.setOnClickListener {
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                editor.putString(Constants.LATITUDE, latLng.latitude.toString())
                editor.putString(Constants.LONGITUDE, latLng.longitude.toString())
                editor.apply()

                alertDialog.dismiss()
            }
        }
    }
}