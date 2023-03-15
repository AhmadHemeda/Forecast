package com.example.forecast.ui.notifications.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.data.model.custom.AlertDateTime
import com.example.forecast.data.utils.dateConverterToString
import com.example.forecast.data.utils.timeConverterToString
import com.example.forecast.databinding.AlertsItemBinding

class NotificationsAdapter(val context: Context, onDateTimeClickListener: OnDateTimeClickListener) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    private var listener = onDateTimeClickListener

    inner class NotificationsViewHolder(val alertsItemBinding: AlertsItemBinding) :
        RecyclerView.ViewHolder(alertsItemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<AlertDateTime>() {
        override fun areItemsTheSame(oldItem: AlertDateTime, newItem: AlertDateTime): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AlertDateTime, newItem: AlertDateTime): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationsAdapter.NotificationsViewHolder {
        val alertsItemBinding =
            AlertsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationsViewHolder(alertsItemBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: NotificationsAdapter.NotificationsViewHolder,
        position: Int
    ) {
        val dateTime = differ.currentList[position]

        holder.alertsItemBinding.textViewFrom.text =
            "${dateConverterToString(dateTime.dateFrom, context)} ${
                timeConverterToString(dateTime.timeFrom, context)}"

        holder.alertsItemBinding.textViewTo.text =
            "${dateConverterToString(dateTime.dateTo, context)} ${
                timeConverterToString(dateTime.timeTo, context)}"

        holder.alertsItemBinding.imageButtonDeleteDateTime.setOnClickListener {
            listener.onDeleteDateTime(dateTime)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    interface OnDateTimeClickListener {
        fun onDeleteDateTime(alertDateTime: AlertDateTime)
    }
}