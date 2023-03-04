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
import com.example.forecast.data.utils.Constants.Companion.SHARED_PREFERENCE
import com.example.forecast.data.utils.Constants.Companion.TEMPERATURE
import com.example.forecast.data.utils.Constants.Companion.WIND_SPEED
import com.example.forecast.databinding.FragmentSettingsBinding
import java.util.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingsViewModel

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        The first variable is sharedPreferences,
         which is assigned to the result of calling getSharedPreferences on the activity object.
          This method returns a shared preferences object that allows accessing and modifying data stored in an XML file with the name given by SHARED_PREFERENCE3.

          The second variable is editor,
           which is assigned to the result of calling edit on the shared preferences object.
            This method returns an editor object that allows modifying values in a shared preferences object and commit them back to disk3.
             */

        val sharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        /*
        The code first checks if there is a value stored for the key LANGUAGE,
         which represents the user’s preferred language.

          If there is, it sets one of the radio buttons (Arabic or English) as checked according to the value (ar or en).

           If there is no value stored, it defaults to en.

           Then, the code sets listeners for each radio button,
             so that when the user clicks on one of them,
              it updates the value of LANGUAGE in the shared preferences using an editor object.

               Then, it creates a new locale object with the corresponding language code1.

                 A locale object represents a specific geographical, political, or cultural region1.

                    The code then sets this locale as the default one and updates the configuration of the resources with this locale2.

                     This changes how texts and other elements are displayed on the screen according to the language.

                     Finally, the code creates an intent object that launches a new activity called MainActivity.

                      An intent object is a way to communicate between different components of an app3.

                       The code then starts this activity using startActivity method.

                        This restarts the app with the new language settings.
                        */

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

        /*
        The code first checks if there is a value stored for the key TEMPERATURE,
         which represents the user’s preferred unit of temperature measurement.

          If there is, it sets one of the radio buttons (Celsius, Fahrenheit or Kelvin) as checked according to the value (metric, imperial or stander).

           If there is no value stored, it defaults to metric.
           Then, the code sets listeners for each radio button,
            so that when the user clicks on one of them,
             it updates the value of TEMPERATURE in the shared preferences using an editor object.

              The editor object allows modifying values in a shared preferences object and commit them back to disk
              */

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

        /*
        The code first checks if there is a value stored for the key WIND_SPEED,
         which represents the user’s preferred unit.

          If there is, it sets one of the radio buttons (MeterSec or MilesHour) as clickable according to the value (metric or imperial).

           If there is no value stored, it defaults to metric.

           Then, the code sets listeners for each radio button,
            so that when the user clicks on one of them,
             it updates the value of WIND_SPEED in the shared preferences using an editor object.

              This changes how wind speed is displayed on the screen according to the unit.
              */

        when (sharedPreferences?.getString(WIND_SPEED, "metric")) {
            "metric" -> binding.radioButtonMeterSec.isClickable = true
            "imperial" -> binding.radioButtonMilesHour.isClickable = true
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}