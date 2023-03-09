package com.example.forecast.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forecast.data.model.Hourly
import com.example.forecast.databinding.HourlyItemBinding
import java.text.SimpleDateFormat

class HourlyAdapter(
    val unit: String,
    val context: Context
) :
    RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {
    private val tempUnit = unit

    inner class HourlyViewHolder(val hourlyItemBinding: HourlyItemBinding) :
        RecyclerView.ViewHolder(hourlyItemBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyAdapter.HourlyViewHolder {
        val hourlyItemBinding =
            HourlyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyViewHolder(hourlyItemBinding)
    }

    override fun onBindViewHolder(holder: HourlyAdapter.HourlyViewHolder, position: Int) {
        val currentHourlyWeatherResponse = differ.currentList[position]

        val iconLink =
            "https://openweathermap.org/img/w/${currentHourlyWeatherResponse.weather[0].icon}.png"
        Glide.with(context).load(iconLink)
            .into(holder.hourlyItemBinding.imageViewConditionIconHourly)

        val unit: String = when (tempUnit) {
            "stander" -> " K"
            "metric" -> " °C"
            else -> " F"
        }

        val timeStamp = currentHourlyWeatherResponse.dt.toLong().times(1000)
        val simpleDateFormatTime = SimpleDateFormat("hh aa")
        val time = simpleDateFormatTime.format(timeStamp)

        holder.hourlyItemBinding.textViewTemperatureHourly.text =
            currentHourlyWeatherResponse.temp.toInt().toString().plus(unit)

        holder.hourlyItemBinding.textViewTime.text = time
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}