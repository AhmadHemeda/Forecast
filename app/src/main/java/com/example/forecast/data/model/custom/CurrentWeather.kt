package com.example.forecast.data.model.custom

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.forecast.data.model.response.OpenWeatherResponse

@Entity(tableName = "current_weather_table")
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val weather: OpenWeatherResponse
)
