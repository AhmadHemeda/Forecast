package com.example.forecast.data.network

import com.example.forecast.data.DataSource
import com.example.forecast.data.model.CurrentWeather
import com.example.forecast.data.model.FavoriteCity
import com.example.forecast.data.model.OpenWeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteDataSource(
    private val openWeatherService: OpenWeatherService
) : DataSource {
    override suspend fun getWeatherDetails(
        lat: Double,
        lon: Double,
        appid: String,
        unit: String
    ): Response<OpenWeatherResponse> {
        return openWeatherService.getWeatherDetails(
            lat, lon, appid, unit
        )
    }

    override fun getCurrentWeather(): Flow<CurrentWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCurrentWeather(currentWeather: CurrentWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather(currentWeather: CurrentWeather) {
        TODO("Not yet implemented")
    }

    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteCities(): Flow<List<FavoriteCity>> {
        TODO("Not yet implemented")
    }
}