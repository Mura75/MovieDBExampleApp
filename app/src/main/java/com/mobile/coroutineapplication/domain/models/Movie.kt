package com.mobile.coroutineapplication.domain.models

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val voteAverage: Double
)