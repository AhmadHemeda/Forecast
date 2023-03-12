package com.example.forecast.data.repo

import com.example.forecast.data.network.ApiState
import com.example.forecast.data.network.OpenWeatherClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class WeatherRepo {

    suspend fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        key: String,
        unit: String
    ) = flow {
        coroutineScope {
            val response = async {
                OpenWeatherClient.api.getWeatherDetails(latitude, longitude, key, unit)
            }

            if (response.await().isSuccessful) {
                val currentData = response.await().body()
                emit(currentData?.let {
                    ApiState.Success(it)
                })
            } else {
                emit(ApiState.Failure(Throwable(response.await().message())))
            }
        }
    }
}