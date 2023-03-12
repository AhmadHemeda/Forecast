package com.example.forecast.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.repo.CurrentWeatherRepo
import com.example.forecast.data.repo.WeatherRepo

class HomeViewModelFactory(
    private val currentWeatherRepo: CurrentWeatherRepo,
    private var weatherRepo: WeatherRepo
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(currentWeatherRepo, weatherRepo) as T
        }

        throw IllegalArgumentException("view model class not found")
    }
}