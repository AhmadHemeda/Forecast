package com.example.forecast.data

import com.example.forecast.data.model.custom.AlertDateTime
import com.example.forecast.data.model.custom.CurrentWeather
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.data.model.response.OpenWeatherResponse
import com.example.forecast.data.repo.DataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeDataSource(
    var favoriteCities: MutableList<FavoriteCity> = mutableListOf(),
    var currentWeatherList: MutableList<CurrentWeather> = mutableListOf(),
    var alertDateTimeList: MutableList<AlertDateTime> = mutableListOf()
) : DataSource {

    override suspend fun getWeatherDetails(
        lat: Double,
        lon: Double,
        appid: String,
        unit: String
    ): Response<OpenWeatherResponse> {
        return Response.success(OpenWeatherResponse())
    }

    override fun getCurrentWeather(): Flow<CurrentWeather> = flow {
        emit(currentWeatherList[0])
    }

    override suspend fun insertCurrentWeather(currentWeather: CurrentWeather) {
        currentWeatherList.add(currentWeather)
    }

    override suspend fun deleteCurrentWeather(currentWeather: CurrentWeather) {
        currentWeatherList.remove(currentWeather)
    }

    override fun getAllFavoriteCities(): Flow<List<FavoriteCity>> = flow {
        emit(favoriteCities)
    }

    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        favoriteCities.add(favoriteCity)
    }

    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
        favoriteCities.remove(favoriteCity)
    }

    override fun getAllDatesTimes(): Flow<List<AlertDateTime>> = flow {
        emit(alertDateTimeList)
    }

    override suspend fun insertDateTime(alertDateTime: AlertDateTime) {
        alertDateTimeList.add(alertDateTime)
    }

    override suspend fun deleteDateTime(alertDateTime: AlertDateTime) {
        alertDateTimeList.remove(alertDateTime)
    }

}