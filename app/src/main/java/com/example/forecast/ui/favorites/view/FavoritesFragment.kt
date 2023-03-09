package com.example.forecast.ui.favorites.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast.data.model.FavoriteCity
import com.example.forecast.data.utils.Constants.Companion.LATITUDE
import com.example.forecast.data.utils.Constants.Companion.LONGITUDE
import com.example.forecast.databinding.FavoriteCityItemBinding
import com.example.forecast.databinding.FragmentFavoriteBinding
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModel
import com.example.forecast.ui.favorites.viewmodel.FavoritesViewModelFactory
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteCityItemBinding: FavoriteCityItemBinding

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

        favoriteCityItemBinding = FavoriteCityItemBinding.inflate(inflater, container, false)

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

            override fun onDeleteCity(city: FavoriteCity) {
                favoriteViewModel.viewModelScope.launch {
                    favoriteViewModel.deleteCity(city)
                }
            }
        }



        val favoriteCityAdapter = FavoriteCityAdapter(requireContext(), listener)

//        val favoriteCityStateFlow = favoriteViewModel.getAllFavoriteCities().asStateFlow()
//
//        lifecycleScope.launch {
//            favoriteCityStateFlow.collect { favoriteCityList ->
//                favoriteCityList.reversed()
//
//                favoriteCityAdapter.differ.submitList(favoriteCityList.toList())
//                binding.recyclerViewFavoriteCity.apply {
//                    adapter = favoriteCityAdapter
//                    layoutManager = LinearLayoutManager(requireContext())
//                }
//            }
//        }

        favoriteViewModel.favoriteCityLiveData.observe(viewLifecycleOwner) {
            favoriteCityList = it
            favoriteCityList.reversed()

            favoriteCityAdapter.differ.submitList(favoriteCityList.toList())
            binding.recyclerViewFavoriteCity.apply {
                adapter = favoriteCityAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

//        favoriteViewModel.getAllFavoriteCities().observe(viewLifecycleOwner) {
//            favoriteCityList = it
//            favoriteCityList.reversed()
//
//            favoriteCityAdapter.differ.submitList(favoriteCityList.toList())
//            binding.recyclerViewFavoriteCity.apply {
//                adapter = favoriteCityAdapter
//                layoutManager = LinearLayoutManager(requireContext())
//            }
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}