package com.example.forecast.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.forecast.data.model.custom.FavoriteCity
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