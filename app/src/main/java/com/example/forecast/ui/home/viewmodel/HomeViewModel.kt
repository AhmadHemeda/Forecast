package com.example.forecast.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.data.model.custom.CurrentWeather
import com.example.forecast.data.model.response.OpenWeatherResponse
import com.example.forecast.data.network.ApiState
import com.example.forecast.data.repo.CurrentWeatherRepo
import com.example.forecast.data.repo.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private var currentWeatherRepo: CurrentWeatherRepo,
    private var weatherRepo: WeatherRepo
) : ViewModel() {

    private var _weatherLiveData = MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherLiveData: StateFlow<ApiState> = _weatherLiveData

    private val _state = MutableStateFlow(CurrentWeather(weather = OpenWeatherResponse()))
    val state: StateFlow<CurrentWeather>
        get() = _state

    init {
        viewModelScope.launch {
            currentWeatherRepo.getCurrentWeather().collect { currentWeather ->
                _state.value = currentWeather
            }
        }
    }

    suspend fun insertCurrentWeather(currentWeather: CurrentWeather) {
        currentWeatherRepo.insertCurrentWeather(currentWeather)
    }

    suspend fun deleteCurrentWeather(currentWeather: CurrentWeather) {
        currentWeatherRepo.deleteCurrentWeather(currentWeather)
    }

    fun getWeatherDetails(latitude: Double, longitude: Double, key: String, unit: String?) {

        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.getWeatherDetails(latitude, longitude, key, unit!!).catch {
                _weatherLiveData.value = ApiState.Failure(it)
            }.collect {
                _weatherLiveData.value = it!!
            }
        }
    }
}