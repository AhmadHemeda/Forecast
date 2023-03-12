package com.example.forecast.data.model.response


import com.google.gson.annotations.SerializedName

data class Minutely(
    val dt: Int,
    val precipitation: Double
)