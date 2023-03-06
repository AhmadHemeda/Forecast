package com.example.forecast.ui.favorites.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.forecast.R
import com.example.forecast.data.model.OpenWeatherResponse
import com.example.forecast.data.utils.Constants
import com.example.forecast.data.utils.Constants.Companion.API_KEY
import com.example.forecast.data.utils.Constants.Companion.LATITUDE
import com.example.forecast.data.utils.Constants.Companion.LONGITUDE
import com.example.forecast.databinding.ActivityFavoritesBinding
import com.example.forecast.databinding.FragmentHomeBinding
import com.example.forecast.ui.home.view.DailyAdapter
import com.example.forecast.ui.home.view.HourlyAdapter
import com.example.forecast.ui.home.viewmodel.HomeViewModel

class FavoritesActivity : AppCompatActivity() {

    private var _binding: ActivityFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = ActivityFavoritesBinding.inflate(layoutInflater)
        val root: View = binding.root
        setContentView(root)

        val latitude = intent.getDoubleExtra(LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(LONGITUDE, 0.0)

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE)!!
        val unit = sharedPreferences.getString(Constants.TEMPERATURE, "metric")
        val wind = sharedPreferences.getString(Constants.WIND_SPEED, "metric")

        homeViewModel.getWeatherDetails(latitude, longitude, API_KEY, unit)

        homeViewModel.weatherLiveData.observe(this) {
            bindCurrentWeather(
                it,
                unit,
                wind
            )

            val hourlyAdapter = HourlyAdapter(unit!!, this)
            binding.recyclerViewHourly.apply {
                adapter = hourlyAdapter
            }
            hourlyAdapter.differ.submitList(it.hourly)

            val dailyAdapter = DailyAdapter(unit, this)
            binding.recyclerViewDaily.apply {
                adapter = dailyAdapter
            }
            dailyAdapter.differ.submitList(it.daily)
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

        binding.textViewTemperature.text =
            openWeatherResponse?.current?.temp?.toInt().toString().plus(tempUnit)
        binding.textViewFeelsLikeTemperature.text =
            openWeatherResponse?.current?.feelsLike?.toInt().toString().plus(tempUnit)

        binding.textViewWind.text =
            openWeatherResponse?.current?.windSpeed.toString().plus(windSpeedUnit)

        binding.textViewHumidity.text = openWeatherResponse?.current?.humidity.toString().plus(" %")
        binding.textViewPressure.text =
            openWeatherResponse?.current?.pressure.toString().plus(" hPa")

        binding.textViewDescription.text =
            openWeatherResponse?.current?.weather?.get(0)?.description

        val iconLink =
            "https://openweathermap.org/img/wn/${openWeatherResponse?.current?.weather?.get(0)?.icon}@2x.png"
        Glide.with(this).load(iconLink).into(binding.imageViewConditionIcon)
    }
}