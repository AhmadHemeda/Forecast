package com.example.forecast.ui.favorites.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.data.model.custom.FavoriteCity
import com.example.forecast.databinding.FavoriteCityItemBinding

class FavoriteCityAdapter(val context: Context, onCityClickListener: OnCityClickListener) :
    RecyclerView.Adapter<FavoriteCityAdapter.FavoriteCityViewHolder>() {

    private var listener = onCityClickListener

    inner class FavoriteCityViewHolder(val favoriteCityItemBinding: FavoriteCityItemBinding) :
        RecyclerView.ViewHolder(favoriteCityItemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<FavoriteCity>() {
        override fun areItemsTheSame(oldItem: FavoriteCity, newItem: FavoriteCity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FavoriteCity, newItem: FavoriteCity): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteCityAdapter.FavoriteCityViewHolder {
        val favoriteCityItemBinding =
            FavoriteCityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteCityViewHolder(favoriteCityItemBinding)
    }

    override fun onBindViewHolder(
        holder: FavoriteCityAdapter.FavoriteCityViewHolder,
        position: Int
    ) {
        val city = differ.currentList[position]

        holder.favoriteCityItemBinding.textViewCityName.text = city.city

        holder.itemView.setOnClickListener {
            listener.onCityClick(city)
        }

        holder.favoriteCityItemBinding.imageButtonDelete.setOnClickListener {
            listener.onDeleteCity(city)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnCityClickListener {
        fun onCityClick(city: FavoriteCity)
        fun onDeleteCity(city: FavoriteCity)
    }
}