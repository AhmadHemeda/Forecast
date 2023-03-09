package com.example.forecast.ui.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.forecast.data.model.OpenWeatherResponse
import com.example.forecast.data.network.ApiState
import com.example.forecast.data.utils.Constants.Companion.API_KEY
import com.example.forecast.data.utils.Constants.Companion.LATITUDE
import com.example.forecast.data.utils.Constants.Companion.LONGITUDE
import com.example.forecast.data.utils.Constants.Companion.SHARED_PREFERENCE
import com.example.forecast.data.utils.Constants.Companion.TEMPERATURE
import com.example.forecast.data.utils.Constants.Companion.WIND_SPEED
import com.example.forecast.databinding.FragmentHomeBinding
import com.example.forecast.databinding.FragmentSettingsBinding
import com.example.forecast.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var settingsBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        val sharedPreferences: SharedPreferences =
            context?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)!!
        val latitude = sharedPreferences.getString(LATITUDE, "0.0")?.toDouble()
        val longitude = sharedPreferences.getString(LONGITUDE, "0.0")?.toDouble()
        val unit = sharedPreferences.getString(TEMPERATURE, "metric")
        val wind = sharedPreferences.getString(WIND_SPEED, "metric")

        val hourlyAdapter = HourlyAdapter(unit!!, requireContext())
        val dailyAdapter = DailyAdapter(unit, requireContext())

        this.homeViewModel = ViewModelProvider(this)[homeViewModel::class.java]
        homeViewModel.getWeatherDetails(latitude!!, longitude!!, API_KEY, unit)

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
                            Toast.makeText(context, it.msg.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }

                        is ApiState.Loading -> {
                            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun bindCurrentWeather(
        openWeatherResponse: OpenWeatherResponse,
        unit: String,
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

        val iconLink =
            "https://openweathermap.org/img/w/${openWeatherResponse.current?.weather?.get(0)?.icon}.png"

        val timeStamp = openWeatherResponse.current?.dt?.times(1000)
        val simpleDateFormatDate = SimpleDateFormat("dd MMM")
        val date = simpleDateFormatDate.format(timeStamp)

        val simpleDateFormatTime = SimpleDateFormat("hh:mm aa")
        val time = simpleDateFormatTime.format(timeStamp)

        binding.textViewTemperature.text =
            openWeatherResponse.current?.temp?.toInt().toString().plus(tempUnit)

        binding.textViewFeelsLikeTemperature.text =
            openWeatherResponse.current?.feelsLike?.toInt().toString().plus(tempUnit)

        binding.textViewWind.text =
            openWeatherResponse.current?.windSpeed.toString().plus(windSpeedUnit)

        binding.textViewHumidity.text = openWeatherResponse.current?.humidity.toString().plus(" %")

        binding.textViewPressure.text =
            openWeatherResponse.current?.pressure.toString().plus(" hPa")

        binding.textViewDescription.text = openWeatherResponse.current?.weather?.get(0)?.description

        binding.textViewCity.text = getCityName(openWeatherResponse.lat, openWeatherResponse.lon)

        binding.textViewDate.text = date

        binding.textViewTime.text = time

        Glide.with(requireContext()).load(iconLink).into(binding.imageViewConditionIcon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCityName(lat: Double, long: Double): String {
        val cityName: String
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 3)

        cityName = address!![0].locality
        return cityName
    }
}