package com.example.forecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather_table")
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val weather: OpenWeatherResponse
)
