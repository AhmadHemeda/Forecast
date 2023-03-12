package com.example.forecast.data.model.geo


import com.google.gson.annotations.SerializedName

data class LocalNames(
    val ar: String,
    val ascii: String,
    val de: String,
    val en: String,
    val eo: String,
    val es: String,
    val et: String,
    @SerializedName("feature_name")
    val featureName: String,
    val fr: String,
    val hi: String,
    val hu: String,
    val pl: String,
    val ru: String,
    val zh: String
)