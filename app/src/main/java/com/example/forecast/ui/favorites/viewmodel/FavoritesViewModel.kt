package com.example.forecast.ui.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.FavoriteCity
import com.example.forecast.data.repo.FavoriteCityRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private var favoriteCityRepo: FavoriteCityRepo) : ViewModel() {

    private val _state = MutableStateFlow<List<FavoriteCity>>(emptyList())
    val state: StateFlow<List<FavoriteCity>>
        get() = _state

    init {
        viewModelScope.launch {
            favoriteCityRepo.getAllFavoriteCities().collect { favoriteCities ->
                _state.value = favoriteCities
            }
        }
    }

    suspend fun insertCity(favoriteCity: FavoriteCity) {
        favoriteCityRepo.insertCity(favoriteCity)
    }

    suspend fun deleteCity(favoriteCity: FavoriteCity) {
        favoriteCityRepo.deleteCity(favoriteCity)
    }
}