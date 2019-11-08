package com.mobile.coroutineapplication.data.respository

import com.mobile.coroutineapplication.data.ApiClient
import com.mobile.coroutineapplication.data.MovieMapper
import com.mobile.coroutineapplication.domain.models.Movie
import com.mobile.coroutineapplication.domain.repository.MovieRepository

class MovieRepositoryImpl : MovieRepository {

    private val mapper = MovieMapper()

    override suspend fun getAllMovies(page: Int): List<Movie> {
        val movieResponse = ApiClient.apiClient.getPopularMovies(page).await()
        val result = movieResponse.movies.map { movieData ->
            val movie = mapper.from(movieData)
            movie
        }
        return result
    }
}