package com.example.forecast.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.data.model.OpenWeatherResponse
import com.example.forecast.data.network.ApiState
import com.example.forecast.data.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    private var _weatherLiveData = MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherLiveData: StateFlow<ApiState> = _weatherLiveData
    private val weatherRepository = WeatherRepository()

    fun getWeatherDetails(latitude: Double, longitude: Double, key: String, unit: String?) {

        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherDetails(latitude, longitude, key, unit!!).catch {
                _weatherLiveData.value = ApiState.Failure(it)
            }.collect {
                _weatherLiveData.value = it!!
            }
        }
    }
}