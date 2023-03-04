package com.example.forecast.ui.favorites.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.forecast.data.model.OpenWeatherResponse
import com.example.forecast.data.utils.Constants.Companion.LAT_LONG
import com.example.forecast.databinding.FragmentFavoriteBinding
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favoriteViewModel =
            ViewModelProvider(this)[FavoritesViewModel::class.java]

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val response = OpenWeatherResponse()

        binding.floatingActionButtonMap.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            intent.putExtra(LAT_LONG, response)

            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}