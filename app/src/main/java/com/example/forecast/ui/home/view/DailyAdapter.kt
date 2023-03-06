package com.example.forecast.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forecast.data.model.Daily
import com.example.forecast.databinding.DailyItemBinding

class DailyAdapter(
    val unit: String,
    val context: Context
) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    private val tempUnit = unit

    inner class DailyViewHolder(val dailyItemBinding: DailyItemBinding) :
        RecyclerView.ViewHolder(dailyItemBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyAdapter.DailyViewHolder {
        val dailyItemBinding =
            DailyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(dailyItemBinding)
    }

    override fun onBindViewHolder(holder: DailyAdapter.DailyViewHolder, position: Int) {
        val currentDailyWeatherResponse = differ.currentList[position]

        val iconLink =
            "https://openweathermap.org/img/wn/${currentDailyWeatherResponse.weather[0].icon}@2x.png"
        Glide.with(context).load(iconLink)
            .into(holder.dailyItemBinding.imageViewConditionIconDaily)

        val unit: String = when (tempUnit) {
            "stander" -> " K"
            "metric" -> " °C"
            else -> " F"
        }

        holder.dailyItemBinding.textViewTemperatureDaily.text =
            currentDailyWeatherResponse.temp.day.toInt().toString().plus(unit)

        holder.dailyItemBinding.textViewFeelsLikeTemperatureDaily.text =
            currentDailyWeatherResponse.temp.day.toInt().toString().plus(unit)

        holder.dailyItemBinding.textViewDescriptionDaily.text =
            currentDailyWeatherResponse.weather[0].description

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}