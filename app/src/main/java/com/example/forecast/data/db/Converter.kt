package com.example.forecast.data.db

import androidx.room.TypeConverter
import com.example.forecast.data.model.OpenWeatherResponse
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun fromStringToWeather(json: String): OpenWeatherResponse {
        return Gson().fromJson(json, OpenWeatherResponse::class.java)
    }

    @TypeConverter
    fun fromWeatherToString(openWeatherResponse: OpenWeatherResponse): String {
        return Gson().toJson(openWeatherResponse)
    }
}

