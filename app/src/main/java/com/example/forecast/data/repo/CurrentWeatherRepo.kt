package com.example.forecast.data.repo

import android.app.Application
import com.example.forecast.data.db.LocalDataSource
import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.custom.CurrentWeather
import kotlinx.coroutines.flow.Flow

class CurrentWeatherRepo(
    private var localDataSource: DataSource
) {

    companion object {
        @Volatile
        private var INSTANCE: CurrentWeatherRepo? = null

        fun getInstance(application: Application): CurrentWeatherRepo {
            return INSTANCE ?: synchronized(this) {
                val dataBase = WeatherDataBase.getInstance(application)

                val localSource = LocalDataSource(
                    currentWeatherDao = dataBase.getCurrentWeatherDao(),
                    favoriteCityDAO = dataBase.getFavoriteCityDao(),
                    alertDateTimeDao = dataBase.getAlertDateTimeDao()
                )

                CurrentWeatherRepo(
                    localDataSource = localSource
                )
            }
        }
    }

    fun getCurrentWeather(): Flow<CurrentWeather> {
        return localDataSource.getCurrentWeather()
    }

    suspend fun insertCurrentWeather(currentWeather: CurrentWeather) {
        localDataSource.insertCurrentWeather(currentWeather)
    }

    suspend fun deleteCurrentWeather(currentWeather: CurrentWeather) {
        localDataSource.deleteCurrentWeather(currentWeather)
    }
}