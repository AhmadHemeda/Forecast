package com.example.forecast.data.model.response


import com.google.gson.annotations.SerializedName

data class Alert(
    val description: String,
    val end: Int,
    val event: String,
    @SerializedName("sender_name")
    val senderName: String,
    val start: Int,
    val tags: List<String>
)