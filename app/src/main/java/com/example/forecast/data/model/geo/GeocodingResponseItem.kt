package com.example.forecast.data.model.geo


import com.google.gson.annotations.SerializedName

data class GeocodingResponseItem(
    val country: String,
    val lat: Double,
    @SerializedName("local_names")
    val localNames: LocalNames,
    val lon: Double,
    val name: String
)