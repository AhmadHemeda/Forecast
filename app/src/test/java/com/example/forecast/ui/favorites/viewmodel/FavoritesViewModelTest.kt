package com.example.forecast.ui.favorites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.forecast.data.network.OpenWeatherService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getState_getCities_expectedSameSize() {
    }

    @Test
    fun insertCity() {
    }

    @Test
    fun deleteCity() {
    }
}