package com.example.forecast.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.MainActivity
import com.example.forecast.data.utils.Constants.Companion.LANGUAGE
import com.example.forecast.data.utils.Constants.Companion.LOCATION
import com.example.forecast.data.utils.Constants.Companion.SHARED_PREFERENCE
import com.example.forecast.data.utils.Constants.Companion.TEMPERATURE
import com.example.forecast.data.utils.Constants.Companion.WIND_SPEED
import com.example.forecast.databinding.FragmentSettingsBinding
import com.example.forecast.ui.favorites.view.MapsActivity
import com.example.forecast.ui.home.view.HomeMap
import java.util.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        when (sharedPreferences?.getString(LANGUAGE, "en")) {
            "ar" -> binding.radioButtonArabic.isChecked = true
            "en" -> binding.radioButtonEnglish.isChecked = true
        }

        binding.radioButtonArabic.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(LANGUAGE, "ar")
                editor?.apply()

                val locale = Locale("ar")
                Locale.setDefault(locale)

                val resources = activity?.resources
                val configuration = resources?.configuration

                configuration?.setLocale(locale)
                resources?.updateConfiguration(configuration, resources.displayMetrics)

                val intentActivity = Intent(requireContext(), MainActivity::class.java)
                startActivity(intentActivity)
            }
        }

        binding.radioButtonEnglish.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(LANGUAGE, "en")
                editor?.apply()

                val locale = Locale("en")
                Locale.setDefault(locale)

                val resources = activity?.resources
                val configuration = resources?.configuration

                configuration?.setLocale(locale)
                resources?.updateConfiguration(configuration, resources.displayMetrics)

                val intentActivity = Intent(requireContext(), MainActivity::class.java)
                startActivity(intentActivity)
            }
        }

        when (sharedPreferences?.getString(TEMPERATURE, "metric")) {
            "metric" -> binding.radioButtonCelsius.isChecked = true
            "imperial" -> binding.radioButtonFahrenheit.isChecked = true
            "stander" -> binding.radioButtonKelvin.isChecked = true
        }

        binding.radioButtonCelsius.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(TEMPERATURE, "metric")
                editor?.apply()
            }
        }

        binding.radioButtonFahrenheit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(TEMPERATURE, "imperial")
                editor?.apply()
            }
        }

        binding.radioButtonKelvin.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(TEMPERATURE, "stander")
                editor?.apply()
            }
        }

        when (sharedPreferences?.getString(WIND_SPEED, "metric")) {
            "metric" -> binding.radioButtonMeterSec.isChecked = true
            "imperial" -> binding.radioButtonMilesHour.isChecked = true
        }

        binding.radioButtonMeterSec.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(WIND_SPEED, "metric")
                editor?.apply()
            }
        }

        binding.radioButtonMilesHour.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(WIND_SPEED, "imperial")
                editor?.apply()
            }
        }

        when (sharedPreferences?.getString(LOCATION, "gps")) {
            "gps" -> binding.radioButtonLocation.isChecked = true
            "map" -> binding.radioButtonMap.isChecked = true
        }

        binding.radioButtonMap.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(LOCATION, "map")
                editor?.apply()

                val intent = Intent(context, HomeMap::class.java)
                startActivity(intent)
            }
        }

        binding.radioButtonLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor?.putString(LOCATION, "gps")
                editor?.apply()

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}