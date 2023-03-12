package com.example.forecast.data.repo

import android.app.Application
import com.example.forecast.data.db.LocalDataSource
import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.custom.FavoriteCity
import kotlinx.coroutines.flow.Flow

class FavoriteCityRepo(
    private var localDataSource: DataSource
) : IFavoriteCityRepo {

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

    override fun getAllFavoriteCities(): Flow<List<FavoriteCity>> {
        return localDataSource.getAllFavoriteCities()
    }

    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        localDataSource.insertCity(favoriteCity)
    }

    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
        localDataSource.deleteCity(favoriteCity)
    }
}