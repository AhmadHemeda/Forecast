package com.example.forecast.data.repo

import com.example.forecast.data.model.custom.AlertDateTime
import kotlinx.coroutines.flow.Flow

interface INotificationRepo {
    fun getAllDatesTimes(): Flow<List<AlertDateTime>>

    suspend fun insertDateTime(alertDateTime: AlertDateTime)

    suspend fun deleteDateTime(alertDateTime: AlertDateTime)
}