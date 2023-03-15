package com.example.forecast.ui.notifications.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.data.model.custom.AlertDateTime
import com.example.forecast.data.repo.INotificationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private var notificationRepo: INotificationRepo
) : ViewModel() {

    private val _stateGetAllDatesTimes = MutableStateFlow<List<AlertDateTime>>(emptyList())
    val stateGetAllDatesTimes: StateFlow<List<AlertDateTime>>
        get() = _stateGetAllDatesTimes

    private val _stateInsertDateTime = MutableStateFlow<Long>(-1)
    val stateInsertDateTime: StateFlow<Long>
        get() = _stateInsertDateTime

    private val _stateGetDateTime = MutableStateFlow<AlertDateTime>(AlertDateTime())
    val stateGetDateTime: StateFlow<AlertDateTime>
        get() = _stateGetDateTime

    init {
        viewModelScope.launch {
            notificationRepo.getAllDatesTimes().collect { allDatesTimes ->
                _stateGetAllDatesTimes.value = allDatesTimes
            }
        }
    }

    fun getDateTime(id: Int) {
        viewModelScope.launch {
            val alertModel = notificationRepo.getDateTime(id)
            _stateGetDateTime.value = alertModel
        }
    }

    fun insertDateTime(alertDateTime: AlertDateTime) {
        viewModelScope.launch {
            val id = notificationRepo.insertDateTime(alertDateTime)
            _stateInsertDateTime.value = id
        }
    }

    fun deleteDateTime(alertDateTime: AlertDateTime) {
        viewModelScope.launch {
            notificationRepo.deleteDateTime(alertDateTime.id)
        }
    }
}