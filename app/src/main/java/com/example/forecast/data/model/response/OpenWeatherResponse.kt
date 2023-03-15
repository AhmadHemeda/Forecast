package com.example.forecast.data.model.response

import com.google.gson.annotations.SerializedName

data class OpenWeatherResponse(
    @SerializedName("alerts")
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