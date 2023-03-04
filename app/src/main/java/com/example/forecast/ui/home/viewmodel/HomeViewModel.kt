package com.example.forecast.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.data.model.OpenWeatherResponse
import com.example.forecast.data.repo.WeatherRepository
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    var weatherLiveData = MutableLiveData<OpenWeatherResponse>()
    val weatherRepository = WeatherRepository()

/*
fun getCurrentWeather(latitude: Double, longitude: Double, key: String, unit: String) {

viewModelScope.launch {
val response = weatherRepository.getCurrentWeather(latitude, longitude, key, unit)

if (response.isSuccessful) {
currentWeatherLiveData.postValue(response.body())
} else {
Log.d(TAG, "getCurrentWeather: Fail")
}
}
}

fun getCurrentWeatherHourly(lat: Double, lon: Double, apikey: String, unit: String) {

viewModelScope.launch {
val response = weatherRepository.getCurrentWeatherDaysAndHourly(lat, lon, apikey, unit)

if (response.isSuccessful) {
hourlyWeatherLiveData.postValue(response.body())
} else {
Log.d(TAG, "getCurrentWeatherHourly: Fail")
}
}
}
*/

    fun getWeatherDetails(latitude: Double, longitude: Double, key: String, unit: String) {

        viewModelScope.launch {
            val response = weatherRepository.getWeatherDetails(latitude, longitude, key, unit)

            if (response.isSuccessful) {
                weatherLiveData.postValue(response.body())
            } else {
                Log.d(TAG, "getWeatherDetails: Fail")
            }
        }
    }
}