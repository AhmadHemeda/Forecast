package com.example.forecast.data.repo

import android.app.Application
import com.example.forecast.data.db.LocalDataSource
import com.example.forecast.data.db.WeatherDataBase
import com.example.forecast.data.model.custom.AlertDateTime
import kotlinx.coroutines.flow.Flow

class NotificationRepo(
    private var localDataSource: DataSource
) : INotificationRepo {

    companion object {
        @Volatile
        private var INSTANCE: NotificationRepo? = null

        fun getInstance(application: Application): NotificationRepo {
            return INSTANCE ?: synchronized(this) {
                val dataBase = WeatherDataBase.getInstance(application)

                val localSource = LocalDataSource(
                    currentWeatherDao = dataBase.getCurrentWeatherDao(),
                    favoriteCityDAO = dataBase.getFavoriteCityDao(),
                    alertDateTimeDao = dataBase.getAlertDateTimeDao()
                )

                NotificationRepo(
                    localDataSource = localSource
                )
            }
        }
    }

    override fun getAllDatesTimes(): Flow<List<AlertDateTime>> {
        return localDataSource.getAllDatesTimes()
    }

    override suspend fun insertDateTime(alertDateTime: AlertDateTime) {
        localDataSource.insertDateTime(alertDateTime)
    }

    override suspend fun deleteDateTime(alertDateTime: AlertDateTime) {
        localDataSource.deleteDateTime(alertDateTime)
    }
}