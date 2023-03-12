package com.example.forecast.data.model.custom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_city_table")
data class FavoriteCity(
    val latitude: Double,
    val longitude: Double,
    val city: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
