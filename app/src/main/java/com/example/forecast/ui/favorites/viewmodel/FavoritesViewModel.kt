package com.example.forecast.ui.favorites.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.FavoriteCity
import com.example.forecast.data.repo.FavoriteCityRepo

class FavoritesViewModel(context: Context) : ViewModel() {

    private val weatherDataBase = WeatherDataBase.getInstance(context)
    private val weatherRepository = FavoriteCityRepo(weatherDataBase)

    fun getAllFavoriteCities(): LiveData<List<FavoriteCity>> {
        return weatherRepository.getAllFavoriteCities()
    }

    suspend fun insertCity(favoriteCity: FavoriteCity) {
        weatherRepository.insertCity(favoriteCity)
    }

    suspend fun deleteCity(favoriteCity: FavoriteCity) {
        weatherRepository.deleteCity(favoriteCity)
    }
}