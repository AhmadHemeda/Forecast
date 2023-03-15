package com.example.forecast.ui.notifications.alert.data

import android.content.Context
import androidx.work.*
import com.example.forecast.data.model.custom.AlertDateTime
import com.example.forecast.data.repo.CurrentWeatherRepo
import com.example.forecast.data.repo.NotificationRepo
import com.example.forecast.data.utils.convertDateToLong
import kotlinx.coroutines.flow.first
import java.util.*
import java.util.concurrent.TimeUnit

class AlertPeriodicWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val currentWeatherRepo = CurrentWeatherRepo.getInstance(context)
    private val notificationRepo = NotificationRepo.getInstance(context)

    override suspend fun doWork(): Result {
        if (!isStopped) {
            val id = inputData.getLong("id", -1)
            getData(id.toInt())
        }
        return Result.success()
    }

    private suspend fun getData(id: Int) {
        val currentWeather = currentWeatherRepo.getCurrentWeather().first().weather
        val alert = notificationRepo.getDateTime(id)

        if (checkTimeLimit(alert)) {
            val delay: Long = getDelay(alert)
            if (currentWeather.alerts.isEmpty()) {
                setOneTimeWorkManger(
                    delay,
                    alert.id,
                    currentWeather.current?.weather?.get(0)?.description ?: "",
                )
            } else {
                setOneTimeWorkManger(
                    delay,
                    alert.id,
                    currentWeather.alerts[0].tags[0],
                )
            }
        } else {
            notificationRepo.deleteDateTime(id)
            WorkManager.getInstance().cancelAllWorkByTag("$id")
        }
    }

    private fun setOneTimeWorkManger(delay: Long, id: Int?, description: String) {
        val data = Data.Builder()
        data.putString("description", description)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()


        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            AlertOneTimeWorkManger::class.java,
        )
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }

    private fun getDelay(alertDateTime: AlertDateTime): Long {
        val hour =
            TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alertDateTime.timeFrom - ((hour + minute) - (2 * 3600L))
    }

    private fun checkTimeLimit(alertDateTime: AlertDateTime): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = convertDateToLong(date, context)

        return dayNow >= alertDateTime.dateFrom
                &&
                dayNow <= alertDateTime.dateTo
    }
}