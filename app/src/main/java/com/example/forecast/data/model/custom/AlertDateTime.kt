package com.example.forecast.data.model.custom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertDateTime(
    var dateFrom: Long = 0,
    var timeFrom: Long = 0,
    var dateTo: Long = 0,
    var timeTo: Long = 0,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}