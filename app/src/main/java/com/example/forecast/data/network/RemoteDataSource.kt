package com.example.forecast.data.network

import com.example.forecast.data.model.custom.AlertDateTime
import com.example.forecast.data.repo.DataSource
import com.example.forecast.data.model.custom.CurrentWeather
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.data.model.response.OpenWeatherResponse
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

    override fun getAllDatesTimes(): Flow<List<AlertDateTime>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDateTime(id: Int): AlertDateTime {
        TODO("Not yet implemented")
    }

    override suspend fun insertDateTime(alertDateTime: AlertDateTime): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDateTime(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteCities(): Flow<List<FavoriteCity>> {
        TODO("Not yet implemented")
    }
}