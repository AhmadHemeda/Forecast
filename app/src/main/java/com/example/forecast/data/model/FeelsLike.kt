package com.example.forecast.data.model


import com.google.gson.annotations.SerializedName

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)