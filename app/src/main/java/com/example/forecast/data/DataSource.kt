package com.example.forecast.data

import com.example.forecast.data.model.CurrentWeather
import com.example.forecast.data.model.FavoriteCity
import com.example.forecast.data.model.OpenWeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

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