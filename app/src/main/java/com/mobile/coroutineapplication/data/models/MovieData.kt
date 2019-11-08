package com.mobile.coroutineapplication.data.models

import com.google.gson.annotations.SerializedName

data class MovieData(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("age") val age: Int
)