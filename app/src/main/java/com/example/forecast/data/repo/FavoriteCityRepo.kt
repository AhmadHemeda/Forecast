package com.example.forecast.data.repo

import android.app.Application
import com.example.forecast.data.DataSource
import com.example.forecast.data.db.LocalDataSource
import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.FavoriteCity
import kotlinx.coroutines.flow.Flow

class FavoriteCityRepo(
    private var localDataSource: DataSource
) {

    companion object {
        @Volatile
        private var INSTANCE: FavoriteCityRepo? = null

        fun getInstance(application: Application): FavoriteCityRepo {
            return INSTANCE ?: synchronized(this) {
                val dataBase = WeatherDataBase.getInstance(application)

                val localSource = LocalDataSource(
                    currentWeatherDao = dataBase.getCurrentWeatherDao(),
                    favoriteCityDAO = dataBase.getFavoriteCityDao()
                )

                FavoriteCityRepo(
                    localDataSource = localSource
                )
            }
        }
    }

    fun getAllFavoriteCities(): Flow<List<FavoriteCity>> {
        return localDataSource.getAllFavoriteCities()
    }

    suspend fun insertCity(favoriteCity: FavoriteCity) {
        localDataSource.insertCity(favoriteCity)
    }

    suspend fun deleteCity(favoriteCity: FavoriteCity) {
        localDataSource.deleteCity(favoriteCity)
    }
}