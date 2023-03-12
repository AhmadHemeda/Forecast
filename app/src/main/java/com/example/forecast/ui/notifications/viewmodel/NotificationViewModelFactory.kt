package com.example.forecast.ui.notifications.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.repo.INotificationRepo
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModel

class NotificationViewModelFactory(var notificationRepo: INotificationRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(notificationRepo) as T
        }

        throw IllegalArgumentException("view model class not found")
    }
}