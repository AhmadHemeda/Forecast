package com.example.forecast.data.model.custom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertDateTime(
    val dateFrom: Long,
    val timeFrom: Long,
    val dateTo: Long,
    val timeTo: Long,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}