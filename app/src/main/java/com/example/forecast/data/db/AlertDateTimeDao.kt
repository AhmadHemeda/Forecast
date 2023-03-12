package com.example.forecast.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.forecast.data.model.custom.AlertDateTime
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDateTimeDao {

    @Insert
    suspend fun insertDateTime(alertDateTime: AlertDateTime)

    @Delete
    suspend fun deleteDateTime(alertDateTime: AlertDateTime)

    @Query("SELECT * FROM alert_table")
    fun getAllDatesTimes(): Flow<List<AlertDateTime>>
}