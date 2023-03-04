package com.example.forecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.forecast.data.model.FavoriteCity

@Database(entities = [FavoriteCity::class], version = 1)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun getFavoriteCityDao(): FavoriteCityDAO

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
        ).build()
    }
}