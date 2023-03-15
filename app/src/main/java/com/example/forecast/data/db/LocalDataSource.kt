package com.example.forecast.data.db

import com.example.forecast.data.db.dao.AlertDateTimeDao
import com.example.forecast.data.db.dao.CurrentWeatherDao
import com.example.forecast.data.db.dao.FavoriteCityDAO
import com.example.forecast.data.model.custom.AlertDateTime
import com.example.forecast.data.repo.DataSource
import com.example.forecast.data.model.custom.CurrentWeather
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.data.model.response.OpenWeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class LocalDataSource(
    private val currentWeatherDao: CurrentWeatherDao,
    private val favoriteCityDAO: FavoriteCityDAO,
    private val alertDateTimeDao: AlertDateTimeDao
) : DataSource {
    override suspend fun getWeatherDetails(
        lat: Double,
        lon: Double,
        appid: String,
        unit: String
    ): Response<OpenWeatherResponse> {
        TODO("Not yet implemented")
    }

    override fun getCurrentWeather(): Flow<CurrentWeather> {
        return currentWeatherDao.getCurrentWeather()
    }

    override suspend fun insertCurrentWeather(currentWeather: CurrentWeather) {
        currentWeatherDao.insertCurrentWeather(currentWeather)
    }

    override suspend fun deleteCurrentWeather(currentWeather: CurrentWeather) {
        currentWeatherDao.deleteCurrentWeather(currentWeather)
    }

    override fun getAllFavoriteCities(): Flow<List<FavoriteCity>> {
        return favoriteCityDAO.getAllFavoriteCities()
    }

    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        favoriteCityDAO.insertCity(favoriteCity)
    }

    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
        favoriteCityDAO.deleteCity(favoriteCity)
    }

    override fun getAllDatesTimes(): Flow<List<AlertDateTime>> {
        return alertDateTimeDao.getAllDatesTimes()
    }

    override suspend fun getDateTime(id: Int): AlertDateTime {
        return alertDateTimeDao.getDateTime(id)
    }

    override suspend fun insertDateTime(alertDateTime: AlertDateTime): Long {
        return alertDateTimeDao.insertDateTime(alertDateTime)
    }

    override suspend fun deleteDateTime(id: Int) {
        alertDateTimeDao.deleteDateTime(id)
    }
}