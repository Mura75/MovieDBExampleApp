package com.mobile.coroutineapplication.domain.repository

import com.mobile.coroutineapplication.domain.models.Movie
import io.reactivex.Single

interface MovieRepository {

    fun getAllMovies(page: Int): Single<List<Movie>>
}