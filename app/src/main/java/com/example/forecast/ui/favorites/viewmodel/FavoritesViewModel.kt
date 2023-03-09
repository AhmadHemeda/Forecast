package com.example.forecast.ui.favorites.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.FavoriteCity
import com.example.forecast.data.repo.FavoriteCityRepo
import kotlinx.coroutines.launch

class FavoritesViewModel(context: Context) : ViewModel() {

    private val weatherDataBase = WeatherDataBase.getInstance(context)
    private val weatherRepository = FavoriteCityRepo(weatherDataBase)

    val favoriteCityLiveData = MutableLiveData<List<FavoriteCity>>()

    init {
        viewModelScope.launch {
            weatherRepository.getAllFavoriteCities().collect { favoriteCities ->
                favoriteCityLiveData.postValue(favoriteCities)
            }
        }
    }

//    fun getAllFavoriteCities() {
//
//    }

    // weatherRepository.getAllFavoriteCities()

    suspend fun insertCity(favoriteCity: FavoriteCity) {
        weatherRepository.insertCity(favoriteCity)
    }

    suspend fun deleteCity(favoriteCity: FavoriteCity) {
        weatherRepository.deleteCity(favoriteCity)
    }
}