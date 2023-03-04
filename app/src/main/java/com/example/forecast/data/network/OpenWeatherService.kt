package com.example.forecast.data.network

import com.example.forecast.data.model.OpenWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

//    @GET("weather")
//    suspend fun getCurrentWeather(
//        @Query("lat") lat: Double,
//        @Query("lon") lon: Double,
//        @Query("appid") appid: String,
//        @Query("units") unit: String,
//    ): Response<CurrentWeatherResponse>
//
//    @GET("weather")
//    suspend fun getCurrentWeatherByCity(
//        @Query("q") q: String,
//        @Query("appid") appid: String,
//        @Query("units") unit: String
//    ): Response<CurrentWeatherResponse>
//
//    @GET("forecast")
//    suspend fun getCurrentWeatherDaysAndHourly(
//        @Query("lat") lat: Double,
//        @Query("lon") lon: Double,
//        @Query("appid") appid: String,
//        @Query("units") unit: String,
//    ): Response<ForecastResponse>

    @GET("onecall")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") unit: String,
    ): Response<OpenWeatherResponse>
}