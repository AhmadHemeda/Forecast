package com.example.forecast.data.network

import com.example.forecast.data.model.response.OpenWeatherResponse

sealed class ApiState {
    class Success(val data: OpenWeatherResponse) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}
