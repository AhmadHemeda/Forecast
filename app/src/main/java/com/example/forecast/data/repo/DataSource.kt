package com.example.forecast.data.repo

import com.example.forecast.data.model.custom.CurrentWeather
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.data.model.response.OpenWeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DataSource {

    suspend fun getWeatherDetails(
        lat: Double,
        on: Double,
        appid: String,
        unit: String,
    ): Response<OpenWeatherResponse>

    fun getCurrentWeather(): Flow<CurrentWeather>

    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    suspend fun deleteCurrentWeather(currentWeather: CurrentWeather)

    fun getAllFavoriteCities(): Flow<List<FavoriteCity>>

    suspend fun insertCity(favoriteCity: FavoriteCity)

    suspend fun deleteCity(favoriteCity: FavoriteCity)
}