package com.example.forecast.ui.favorites.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.forecast.data.model.response.OpenWeatherResponse
import com.example.forecast.data.network.ApiState
import com.example.forecast.data.repo.CurrentWeatherRepo
import com.example.forecast.data.repo.WeatherRepo
import com.example.forecast.data.utils.Constants
import com.example.forecast.data.utils.Constants.Companion.API_KEY
import com.example.forecast.data.utils.Constants.Companion.LATITUDE
import com.example.forecast.data.utils.Constants.Companion.LONGITUDE
import com.example.forecast.data.utils.IconMapper
import com.example.forecast.data.utils.getCurrentLocale
import com.example.forecast.databinding.ActivityFavoritesBinding
import com.example.forecast.ui.home.view.DailyAdapter
import com.example.forecast.ui.home.view.HourlyAdapter
import com.example.forecast.ui.home.viewmodel.HomeViewModel
import com.example.forecast.ui.home.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FavoritesActivity : AppCompatActivity() {

    private var _binding: ActivityFavoritesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        val homeViewModelFactory =
            HomeViewModelFactory(
                CurrentWeatherRepo.getInstance(application),
                WeatherRepo()
            )

        val homeViewModel =
            ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

        _binding = ActivityFavoritesBinding.inflate(layoutInflater)
        val root: View = binding.root
        setContentView(root)

        val latitude = intent.getDoubleExtra(LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(LONGITUDE, 0.0)

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)!!
        val unit = sharedPreferences.getString(Constants.TEMPERATURE, "metric")
        val wind = sharedPreferences.getString(Constants.WIND_SPEED, "metric")

        val hourlyAdapter = HourlyAdapter(unit!!, this)
        val dailyAdapter = DailyAdapter(unit, this)

        homeViewModel.getWeatherDetails(latitude, longitude, API_KEY, unit)

        lifecycleScope.launch {
            homeViewModel.weatherLiveData
                .collect {
                    when (it) {
                        is ApiState.Success -> {
                            val date = it.data

                            bindCurrentWeather(
                                date,
                                unit,
                                wind
                            )

                            binding.recyclerViewHourly.apply {
                                adapter = hourlyAdapter
                            }
                            hourlyAdapter.differ.submitList(date.hourly)


                            binding.recyclerViewDaily.apply {
                                adapter = dailyAdapter
                            }
                            dailyAdapter.differ.submitList(date.daily)
                        }

                        is ApiState.Failure -> {

                        }

                        is ApiState.Loading -> {

                        }
                    }
                }
        }
    }

    private fun bindCurrentWeather(
        openWeatherResponse: OpenWeatherResponse?,
        unit: String?,
        wind: String?
    ) {
        val tempUnit: String = when (unit) {
            "stander" -> " K"
            "metric" -> " °C"
            else -> " F"
        }

        val windSpeedUnit: String = when (wind) {
            "stander" -> " m/s"
            "metric" -> " m/s"
            else -> " m/h"
        }

        val timeStamp = openWeatherResponse?.current?.dt?.times(1000)

        val simpleDateFormatDate = SimpleDateFormat("dd MMM", getCurrentLocale(this))
        val date = simpleDateFormatDate.format(timeStamp)

        val simpleDateFormatTime = SimpleDateFormat("hh:mm aa", getCurrentLocale(this))
        val time = simpleDateFormatTime.format(timeStamp)

        val icon = openWeatherResponse?.current?.weather?.get(0)?.icon

        binding.textViewTemperature.text =
            openWeatherResponse?.current?.temp?.toInt().toString().plus(tempUnit)

        binding.textViewFeelsLikeTemperature.text =
            openWeatherResponse?.current?.feelsLike?.toInt().toString().plus(tempUnit)

        if (openWeatherResponse != null) {
            binding.textViewCity.text =
                getCityName(openWeatherResponse.lat, openWeatherResponse.lon)
        }

        binding.textViewWind.text =
            openWeatherResponse?.current?.windSpeed.toString().plus(windSpeedUnit)

        binding.textViewHumidity.text =
            openWeatherResponse?.current?.humidity.toString().plus(" %")

        binding.textViewPressure.text =
            openWeatherResponse?.current?.pressure.toString().plus(" hPa")

        binding.textViewClouds.text =
            openWeatherResponse?.current?.clouds.toString().plus(" %")

        binding.textViewVisibility.text =
            openWeatherResponse?.current?.visibility?.times(0.001)?.toInt().toString().plus(" km")

        binding.textViewUvi.text =
            openWeatherResponse?.current?.uvi.toString()

        binding.textViewDate.text = date

        binding.textViewTime.text = time

        binding.imageViewConditionIcon.setImageResource(IconMapper.getWeatherIcon(icon!!))

        binding.textViewDescription.text =
            openWeatherResponse.current.weather[0].description
    }

    private fun getCityName(lat: Double, long: Double): String {
        val cityName: String
        val geoCoder = getCurrentLocale(this)?.let { Geocoder(this, it) }
        val address = geoCoder?.getFromLocation(lat, long, 3)

        cityName = address?.get(0)!!.locality
        return cityName
    }
}