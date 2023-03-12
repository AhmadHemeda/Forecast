package com.example.forecast.data.repo

import com.example.forecast.data.model.custom.FavoriteCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeFavoriteCityRepoTest(
    private var favoriteCities: MutableList<FavoriteCity> = mutableListOf()
) : IFavoriteCityRepo {


    override fun getAllFavoriteCities(): Flow<List<FavoriteCity>> = flow {
        emit(favoriteCities)
    }

    override suspend fun insertCity(favoriteCity: FavoriteCity) {
        favoriteCities.add(favoriteCity)
    }

    override suspend fun deleteCity(favoriteCity: FavoriteCity) {
        favoriteCities.remove(favoriteCity)
    }

}