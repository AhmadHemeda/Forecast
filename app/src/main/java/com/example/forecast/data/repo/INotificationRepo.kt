package com.example.forecast.data.repo

import com.example.forecast.data.model.custom.AlertDateTime
import kotlinx.coroutines.flow.Flow

interface INotificationRepo {
    fun getAllDatesTimes(): Flow<List<AlertDateTime>>

    suspend fun getDateTime(id: Int): AlertDateTime

    suspend fun insertDateTime(alertDateTime: AlertDateTime): Long

    suspend fun deleteDateTime(id: Int)
}