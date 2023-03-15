package com.example.forecast.ui.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forecast.data.model.response.Daily
import com.example.forecast.data.utils.IconMapper
import com.example.forecast.data.utils.getCurrentLocale
import com.example.forecast.databinding.DailyItemBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle

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

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DailyAdapter.DailyViewHolder, position: Int) {
        val currentDailyWeatherResponse = differ.currentList[position]

        val icon = currentDailyWeatherResponse.weather[0].icon


        val unit: String = when (tempUnit) {
            "stander" -> " K"
            "metric" -> " °C"
            else -> " F"
        }

        val timeStamp = currentDailyWeatherResponse.dt.toLong().times(1000)
        val simpleDateFormatDate = SimpleDateFormat("yyyy-MM-dd")
        val date = simpleDateFormatDate.format(timeStamp)
        val localDate = LocalDate.parse(date)
        val dayName = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, getCurrentLocale(context))

        holder.dailyItemBinding.imageViewConditionIconDaily.setImageResource(IconMapper.getWeatherIcon(icon))

        holder.dailyItemBinding.textViewTemperatureDailyMax.text =
            currentDailyWeatherResponse.temp.max.toInt().toString().plus(unit)

        holder.dailyItemBinding.textViewTemperatureDailyMin.text =
            currentDailyWeatherResponse.temp.min.toInt().toString().plus(unit)

        holder.dailyItemBinding.textViewDescriptionDaily.text =
            currentDailyWeatherResponse.weather[0].description

        holder.dailyItemBinding.textViewDateDaily.text = dayName
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