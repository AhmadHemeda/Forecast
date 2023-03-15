package com.example.forecast.data.db.dao

import androidx.room.*
import com.example.forecast.data.model.custom.AlertDateTime
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDateTimeDao {

    @Query("SELECT * FROM alert_table")
    fun getAllDatesTimes(): Flow<List<AlertDateTime>>

    @Query("SELECT * FROM alert_table WHERE id = :id")
    suspend fun getDateTime(id: Int): AlertDateTime

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDateTime(alertDateTime: AlertDateTime): Long

    @Query("DELETE FROM alert_table WHERE id = :id")
    suspend fun deleteDateTime(id: Int)
}