package com.example.forecast.ui.notifications.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forecast.data.model.custom.AlertDateTime
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.data.repo.INotificationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private var notificationRepo: INotificationRepo
) : ViewModel() {

    private val _state = MutableStateFlow<List<AlertDateTime>>(emptyList())
    val state: StateFlow<List<AlertDateTime>>
        get() = _state

    init {
        viewModelScope.launch {
            notificationRepo.getAllDatesTimes().collect { allDatesTimes ->
                _state.value = allDatesTimes
            }
        }
    }

    suspend fun insertDateTime(alertDateTime: AlertDateTime) {
        notificationRepo.insertDateTime(alertDateTime)
    }

    suspend fun deleteDateTime(alertDateTime: AlertDateTime) {
        notificationRepo.deleteDateTime(alertDateTime)
    }
}