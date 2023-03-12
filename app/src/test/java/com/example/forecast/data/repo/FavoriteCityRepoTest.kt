package com.example.forecast.data.repo

import com.example.forecast.data.FakeDataSource
import com.example.forecast.data.model.custom.CurrentWeather
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.data.model.response.OpenWeatherResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FavoriteCityRepoTest {

    private var favoriteCities: MutableList<FavoriteCity> = mutableListOf(
        FavoriteCity(32.32, 30.30, "Tanta"),
        FavoriteCity(32.32, 30.30, "Tanta"),
        FavoriteCity(32.32, 30.30, "Tanta")
    )

    private var currentWeatherList: MutableList<CurrentWeather> = mutableListOf(
        CurrentWeather(1, OpenWeatherResponse()),
        CurrentWeather(1, OpenWeatherResponse())
    )

    private lateinit var localDataSource: FakeDataSource
    private lateinit var favoriteCityRepo: FavoriteCityRepo

    @Before
    fun init() {
        localDataSource = FakeDataSource(
            favoriteCities,
            currentWeatherList
        )

        favoriteCityRepo = FavoriteCityRepo(localDataSource = localDataSource)
    }

    @Test
    fun getAllFavoriteCities() {

        // Given
        val expected = favoriteCities

        // When
        val actual = favoriteCityRepo.getAllFavoriteCities()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun insertCity() = runBlocking {

        // Given
        val newCity = FavoriteCity(40.40, 50.50, "New York")
        val expected = favoriteCities + newCity

        // When
        favoriteCityRepo.insertCity(newCity)
        val actual = localDataSource.favoriteCities

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun deleteCity() = runBlocking {

        // Given
        val cityToDelete = favoriteCities[0]
        val expected = favoriteCities - cityToDelete

        // When
        favoriteCityRepo.deleteCity(cityToDelete)
        val actual = localDataSource.favoriteCities

        // Then
        assertEquals(expected, actual)
    }
}