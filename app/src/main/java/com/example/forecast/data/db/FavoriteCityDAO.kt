package com.example.forecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.forecast.data.model.FavoriteCity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCityDAO {

    @Insert
    suspend fun insertCity(favoriteCity: FavoriteCity)

    @Delete
    suspend fun deleteCity(favoriteCity: FavoriteCity)

    @Query("SELECT * FROM favorite_city_table")
    fun getAllFavoriteCities(): Flow<List<FavoriteCity>>
}