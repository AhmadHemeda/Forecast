package com.example.forecast.data.repo

import com.example.forecast.data.network.OpenWeatherClient

class WeatherRepository {

/*
suspend fun getCurrentWeather(
latitude: Double,
longitude: Double,
key: String,
unit: String
) =
OpenWeatherClient.api.getCurrentWeather(latitude, longitude, key, unit)

suspend fun getCurrentWeatherDaysAndHourly(
latitude: Double,
longitude: Double,
key: String,
unit: String
) =
OpenWeatherClient.api.getCurrentWeatherDaysAndHourly(latitude, longitude, key, unit)
*/

    suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        key: String,
        unit: String
    ) = OpenWeatherClient.api.getWeatherDetails(latitude, longitude, key, unit)
}