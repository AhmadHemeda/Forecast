package com.example.forecast.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.forecast.data.model.FavoriteCity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteCityDAOTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var dataBase: WeatherDataBase
    lateinit var dao: FavoriteCityDAO

    @Before
    fun setUp() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).allowMainThreadQueries().build()

        dao = dataBase.getFavoriteCityDao()
    }

    @After
    fun tearDown() {
        dataBase.close()
    }

    @Test
    fun insertCity_insertSingleCity_returnCity() = runBlockingTest {

        // Given
        val data = FavoriteCity(30.30, 31.00, "Tanta")

        // When
        dao.insertCity(data)

        // Then
        val result = dao.getAllFavoriteCities().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())
    }

    @Test
    fun deleteCity_deleteCity_returnNull() = runBlockingTest{
        // Given
        val data = FavoriteCity(30.30, 31.00, "Tanta")
        dao.insertCity(data)

        // When
        val result = dao.getAllFavoriteCities().first()
        dao.deleteCity(result[0])

        // Then
        MatcherAssert.assertThat(result.size, Is.`is`(0))
        MatcherAssert.assertThat(result, IsEmptyCollection.empty())
    }

    @Test
    fun getAllFavoriteCities_insertFavoriteCities_countOfCities() = runBlockingTest {

        // Given
        val data1 = FavoriteCity(30.30, 31.00, "Tanta")
        dao.insertCity(data1)

        val data2 = FavoriteCity(50.30, 51.00, "Tokyo")
        dao.insertCity(data2)

        val data3 = FavoriteCity(-20.30, -12.00, "Ontario")
        dao.insertCity(data3)

        // When
        val result = dao.getAllFavoriteCities().first()

        // Then
        MatcherAssert.assertThat(result.size, Is.`is`(3))
    }
}