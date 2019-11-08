package com.mobile.coroutineapplication.data

import com.mobile.coroutineapplication.data.models.MovieData
import com.mobile.coroutineapplication.domain.Mapper
import com.mobile.coroutineapplication.domain.models.Movie

class MovieMapper : Mapper<MovieData, Movie> {

    override fun from(item: MovieData): Movie {
        return Movie(
                id = item.id,
                title = item.title,
                overview = item.overview,
                voteAverage = item.voteAverage
            )
    }

    override fun to(item: Movie): MovieData {
        return MovieData(
            id = item.id,
            title = item.title,
            overview = item.overview,
            voteAverage = item.voteAverage,
            age = 0
        )
    }
}