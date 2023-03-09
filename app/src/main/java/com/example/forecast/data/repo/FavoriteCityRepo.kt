package com.example.forecast.data.repo

import androidx.lifecycle.LiveData
import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.FavoriteCity
import kotlinx.coroutines.flow.Flow

class FavoriteCityRepo(private var weatherDataBase: WeatherDataBase) {

    fun getAllFavoriteCities(): Flow<List<FavoriteCity>> {
        return weatherDataBase.getFavoriteCityDao(). getAllFavoriteCities()
    }

    suspend fun insertCity(favoriteCity: FavoriteCity) {
        weatherDataBase.getFavoriteCityDao().insertCity(favoriteCity)
    }

    suspend fun deleteCity(favoriteCity: FavoriteCity) {
        weatherDataBase.getFavoriteCityDao().deleteCity(favoriteCity)
    }
}