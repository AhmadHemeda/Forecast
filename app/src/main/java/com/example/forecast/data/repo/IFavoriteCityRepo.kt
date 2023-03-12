package com.example.forecast.data.repo

import com.example.forecast.data.model.custom.FavoriteCity
import kotlinx.coroutines.flow.Flow

interface IFavoriteCityRepo {
    fun getAllFavoriteCities(): Flow<List<FavoriteCity>>

    suspend fun insertCity(favoriteCity: FavoriteCity)

    suspend fun deleteCity(favoriteCity: FavoriteCity)
}