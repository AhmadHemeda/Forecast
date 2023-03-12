package com.example.forecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecast.data.model.custom.CurrentWeather
import com.example.forecast.data.model.custom.FavoriteCity

@Database(entities = [FavoriteCity::class, CurrentWeather::class], version = 1)
@TypeConverters(Converter::class)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun getFavoriteCityDao(): FavoriteCityDAO

    abstract fun getCurrentWeatherDao(): CurrentWeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        fun getInstance(context: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(Any()) {
                INSTANCE ?: createDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherDataBase::class.java,
            "weather_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}