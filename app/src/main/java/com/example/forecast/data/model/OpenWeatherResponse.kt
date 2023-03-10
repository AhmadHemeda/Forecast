package com.example.forecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class OpenWeatherResponse(
    val alerts: List<Alert> = listOf(),
    val current: Current? = null,
    val daily: List<Daily> = listOf(),
    val hourly: List<Hourly> = listOf(),
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val minutely: List<Minutely> = listOf(),
    val timezone: String = "",
    val timezoneOffset: Int = 0
)