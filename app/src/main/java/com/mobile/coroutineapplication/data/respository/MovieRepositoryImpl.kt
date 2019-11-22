package com.mobile.coroutineapplication.data.respository

import com.mobile.coroutineapplication.data.ApiClient
import com.mobile.coroutineapplication.data.MovieDBApi
import com.mobile.coroutineapplication.data.MovieMapper
import com.mobile.coroutineapplication.domain.models.Movie
import com.mobile.coroutineapplication.domain.repository.MovieRepository
import io.reactivex.Single

class MovieRepositoryImpl(
    private val movieDBApi: MovieDBApi
) : MovieRepository {

    private val mapper = MovieMapper()

    override fun getAllMovies(page: Int): Single<List<Movie>> {
        return movieDBApi.getPopularMovies(page = page)
            .map { response -> response.movies.map { mapper.from(it) } }
    }
}