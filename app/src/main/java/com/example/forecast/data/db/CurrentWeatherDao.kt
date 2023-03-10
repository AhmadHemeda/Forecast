package com.example.forecast.data.db

import androidx.room.*
import com.example.forecast.data.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM current_weather_table")
    fun getCurrentWeather(): Flow<CurrentWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    @Delete
    suspend fun deleteCurrentWeather(currentWeather: CurrentWeather)
}