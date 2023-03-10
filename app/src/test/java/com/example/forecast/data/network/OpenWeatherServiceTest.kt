package com.example.forecast.data.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.forecast.data.utils.Constants.Companion.API_KEY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class OpenWeatherServiceTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var api: OpenWeatherService

    @Before
    fun setUp() {
        api = OpenWeatherClient.api
    }

    @After
    fun tearDown() {

    }

    @Test
    fun getWeatherDetails_APIRequest_successfulCodeResponse() = runBlocking {
        // Given
        val latitude = 30.7865083
        val longitude = 31.000375
        val apiKey = API_KEY
        val unit = "metric"

        // When
        val response = async {
            api.getWeatherDetails(
                latitude,
                longitude,
                apiKey,
                unit
            )
        }

        // Then
        assertThat(response.await().code(), Is.`is`(200))
    }

    @Test
    fun getWeatherDetails_noKey_unauthorized() = runBlocking {
        // Given
        val latitude = 30.7865083
        val longitude = 31.000375
        val apiKey = ""
        val unit = "metric"

        // When
        val response = async {
            api.getWeatherDetails(
                latitude,
                longitude,
                apiKey,
                unit
            )
        }

        // Then
        assertThat(response.await().code(), Is.`is`(401))
    }
}