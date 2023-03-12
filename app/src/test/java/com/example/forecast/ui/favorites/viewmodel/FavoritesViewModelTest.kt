package com.example.forecast.ui.favorites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.data.repo.FakeFavoriteCityRepoTest
import com.example.forecast.data.repo.IFavoriteCityRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var favoriteCities: MutableList<FavoriteCity> = mutableListOf(
        FavoriteCity(32.32, 30.30, "Tanta"),
        FavoriteCity(32.32, 30.30, "Tanta"),
        FavoriteCity(32.32, 30.30, "Tanta")
    )

    private lateinit var favoriteCityRepo: IFavoriteCityRepo
    private lateinit var favoritesViewModel: FavoritesViewModel

    @Before
    fun setUp() {
        favoriteCityRepo = FakeFavoriteCityRepoTest(
            favoriteCities
        )
        favoritesViewModel = FavoritesViewModel(favoriteCityRepo)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getState_getCities_expectedSameSize() = mainCoroutineRule.runBlockingTest {

        // When
        val data = favoritesViewModel.state.value

        // Then
        assertThat(data.size, Is.`is`(3))
    }

    @Test
    fun insertCity_addNewCity_sizeOfTheListIncreased() = mainCoroutineRule.runBlockingTest {

        // Given
        val favoriteCity =
            FavoriteCity(32.32, 30.30, "Tanta")

        // When
        favoritesViewModel.insertCity(favoriteCity)

        // Then
        assertThat(favoriteCities.size, Is.`is`(4))
    }

    @Test
    fun deleteCity() = mainCoroutineRule.runBlockingTest {

        // Given
        val favoriteCity =
            favoriteCities[0]

        // When
        favoritesViewModel.deleteCity(favoriteCity)

        // Then
        assertThat(favoriteCities.size, Is.`is`(2))
    }
}