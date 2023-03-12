package com.example.forecast.data.model.response


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h: Double
)