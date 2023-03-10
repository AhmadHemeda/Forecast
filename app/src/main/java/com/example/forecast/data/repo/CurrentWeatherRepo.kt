package com.example.forecast.data.repo

import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

class CurrentWeatherRepo(private var weatherDataBase: WeatherDataBase) {

    fun getCurrentWeather(): Flow<CurrentWeather> {
        return weatherDataBase.getCurrentWeatherDao().getCurrentWeather()
    }

    suspend fun insertCurrentWeather(currentWeather: CurrentWeather) {
        weatherDataBase.getCurrentWeatherDao().insertCurrentWeather(currentWeather)
    }

    suspend fun deleteCurrentWeather(currentWeather: CurrentWeather) {
        weatherDataBase.getCurrentWeatherDao().deleteCurrentWeather(currentWeather)
    }
}