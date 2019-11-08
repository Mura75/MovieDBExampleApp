package com.mobile.coroutineapplication.domain.repository

import com.mobile.coroutineapplication.domain.models.Movie

interface MovieRepository {

    suspend fun getAllMovies(page: Int): List<Movie>
}