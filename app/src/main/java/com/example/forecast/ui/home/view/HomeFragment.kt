package com.example.forecast.ui.home.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.forecast.data.model.OpenWeatherResponse
import com.example.forecast.data.utils.Constants.Companion.API_KEY
import com.example.forecast.data.utils.Constants.Companion.LATITUDE
import com.example.forecast.data.utils.Constants.Companion.LONGITUDE
import com.example.forecast.data.utils.Constants.Companion.SHARED_PREFERENCE
import com.example.forecast.data.utils.Constants.Companion.TEMPERATURE
import com.example.forecast.data.utils.Constants.Companion.WIND_SPEED
import com.example.forecast.databinding.FragmentHomeBinding
import com.example.forecast.ui.home.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        The first variable is sharedPreferences,
         which is assigned to the result of calling getSharedPreferences on the context object.

          This method returns a shared preferences object that allows accessing and modifying data stored in an XML file with the name given by SHARED_PREFERENCE.

           The code uses the !! operator to assert that this object is not null.

           The second variable is latitude,
            which is assigned to the value stored for the key LATITUDE in the shared preferences object.

             This value represents the user’s latitude coordinate.

              The code uses the ?. operator to check if this value is null before calling toDouble() on it,
               which converts it from a string to a double. If it is null, it defaults to "0.0".

               The third variable is longitude,
                which is similar to latitude, but for the user’s longitude coordinate.

                The fourth variable is unit,
                 which is assigned to the value stored for the key TEMPERATURE in the shared preferences object.

                  This value represents the user’s preferred unit for temperature (metric or imperial).

                  The fifth variable is wind,
                   which is similar to unit, but for the user’s preferred unit for wind speed (metric or imperial).
                   */

        val sharedPreferences: SharedPreferences =
            context?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)!!
        val latitude = sharedPreferences.getString(LATITUDE, "0.0")?.toDouble()
        val longitude = sharedPreferences.getString(LONGITUDE, "0.0")?.toDouble()
        val unit = sharedPreferences.getString(TEMPERATURE, "metric")
        val wind = sharedPreferences.getString(WIND_SPEED, "metric")

/*
this.homeViewModel = ViewModelProvider(this)[homeViewModel::class.java]
homeViewModel.getCurrentWeather(latitude!!, longitude!!, API_KEY, unit!!)
homeViewModel.currentWeatherLiveData.observe(viewLifecycleOwner) {
bindCurrentWeather(
it,
unit
)
}
*/

        /*
        The code first assigns this.homeViewModel to an instance of homeViewModel class that is obtained from calling ViewModelProvider(this)[homeViewModel::class.java].

         This creates or returns an existing ViewModel associated with this scope1.

          A ViewModel is a class that holds and manages UI-related data in a lifecycle-conscious way1.

          Then, the code calls getWeatherDetails on homeViewModel,
           passing the latitude, longitude, API key, and unit as parameters.

            This method presumably fetches the weather data from an API using these parameters.

            Then, the code observes the weatherLiveData property of homeViewModel,
             which is a LiveData object that holds the weather data.

              A LiveData object is an observable data holder that respects the lifecycle of other app components2.

               The code passes a lambda function that takes an argument it, which represents the weather data.

               Inside this function,
                the code calls bindCurrentWeather,
                 passing it, unit, and wind as parameters.

                  This method presumably updates the UI elements with the current weather information.

                  Then, the code creates two adapters: one for hourly weather and one for daily weather.

                   An adapter is a class that connects a RecyclerView (a view that displays a large set of data efficiently) with its data source3.

                    The code passes unit and context as parameters to each adapter’s constructor.

                    Then, the code applies each adapter to its corresponding RecyclerView using binding.recyclerViewHourly.apply and binding.recyclerViewDaily.apply.

                     The apply function runs a block of code on an object and returns it.

                     Finally, the code calls differ.submitList on each adapter, passing it.hourly and it.daily as parameters.

                      This method submits a new list of items to be displayed by each RecyclerView.
                      */

        this.homeViewModel = ViewModelProvider(this)[homeViewModel::class.java]
        homeViewModel.getWeatherDetails(latitude!!, longitude!!, API_KEY, unit!!)
        homeViewModel.weatherLiveData.observe(viewLifecycleOwner) {
            bindCurrentWeather(
                it,
                unit,
                wind
            )

            val hourlyAdapter = HourlyAdapter(unit = unit, requireContext())
            binding.recyclerViewHourly.apply {
                adapter = hourlyAdapter
            }
            hourlyAdapter.differ.submitList(it.hourly)

            val dailyAdapter = DailyAdapter(unit = unit, requireContext())
            binding.recyclerViewDaily.apply {
                adapter = dailyAdapter
            }
            dailyAdapter.differ.submitList(it.daily)
        }

        return root
    }

    /*
    The function first declares two variables using the val keyword: tempUnit and windSpeedUnit.

     These variables are assigned to different strings depending on the value of unit and wind using a when expression.

      A when expression is similar to a switch statement in other languages.

      The function then updates several UI elements using the data from openWeatherResponse, which is an object that holds the weather information from an API.

       The function uses the binding object to access these UI elements by their IDs.

       The function also appends the appropriate unit strings to some of the data using the plus operator, which concatenates strings.

       The function also formats some of the data using methods like toInt() and toString(),
        which convert values between different types.

        The function also sets the text of textViewDescription and textViewCity to the description and timezone properties of openWeatherResponse.

        Finally, the function constructs a URL for an icon image based on the icon property of openWeatherResponse.

         The function then uses Glide, an image loading and caching library for Android1,
          to load this image into imageViewConditionIcon.

           The function uses methods like with(), load(), and into() from Glide to specify the context, source, and destination of the image loading operation.
           */

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

        val iconLink =
            "https://openweathermap.org/img/wn/${openWeatherResponse.current?.weather?.get(0)?.icon}@2x.png"
        Glide.with(requireContext()).load(iconLink).into(binding.imageViewConditionIcon)

/*
binding.textViewWind.text =
currentWeatherResponse!!.wind.speed.toString().plus(windSpeedUnit)
binding.textViewTemperature.text =
currentWeatherResponse.main.temp.toString().plus(tempUnit)
binding.textViewHumidity.text = currentWeatherResponse.main.humidity.toString().plus(" %")
binding.textViewDescription.text = currentWeatherResponse.weather[0].description
binding.textViewPressure.text = currentWeatherResponse.main.pressure.toString().plus(" hPa")
binding.textViewCity.text = currentWeatherResponse.sys.country
binding.textViewFeelsLikeTemperature.text =
currentWeatherResponse.main.feels_like.toString().plus(tempUnit)

val iconLink =
"https://openweathermap.org/img/wn/${currentWeatherResponse.weather[0].icon}@2x.png"
Glide.with(requireContext()).load(iconLink).into(binding.imageViewConditionIcon)
*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}