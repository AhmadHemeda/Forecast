package com.example.forecast.ui.favorites.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast.data.model.FavoriteCity
import com.example.forecast.data.utils.Constants.Companion.LATITUDE
import com.example.forecast.data.utils.Constants.Companion.LONGITUDE
import com.example.forecast.databinding.FragmentFavoriteBinding
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModel
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModelFactory

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
        val favoritesViewModelFactory = FavoritesViewModelFactory(requireContext())
        val favoriteViewModel =
            ViewModelProvider(this, favoritesViewModelFactory)[FavoritesViewModel::class.java]
        var favoriteCityList: List<FavoriteCity>

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.floatingActionButtonMap.setOnClickListener {
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }

        val listener = object : FavoriteCityAdapter.OnCityClickListener {
            override fun onCityClick(city: FavoriteCity) {
                val intent = Intent(requireContext(), FavoritesActivity::class.java)
                intent.putExtra(LATITUDE, city.latitude)
                intent.putExtra(LONGITUDE, city.longitude)
                startActivity(intent)
            }
        }

        val favoriteCityAdapter = FavoriteCityAdapter(requireContext(), listener)

        favoriteViewModel.getAllFavoriteCities().observe(viewLifecycleOwner) {
            favoriteCityList = it
            favoriteCityList.reversed()

            favoriteCityAdapter.differ.submitList(favoriteCityList.toList())
            binding.recyclerViewFavoriteCity.apply {
                adapter = favoriteCityAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}