package com.example.forecast.ui.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.repo.FavoriteCityRepo
import com.example.forecast.data.repo.IFavoriteCityRepo

class FavoritesViewModelFactory(var favoriteCityRepo: IFavoriteCityRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(favoriteCityRepo) as T
        }

        throw IllegalArgumentException("view model class not found")
    }
}