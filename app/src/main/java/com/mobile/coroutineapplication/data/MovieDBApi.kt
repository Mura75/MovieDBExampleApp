package com.mobile.coroutineapplication.data

import com.google.gson.JsonObject
import com.mobile.coroutineapplication.data.models.MovieListResponse
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MovieDBApi {

    @GET("authentication/token/new")
    fun createRequestTokenCall(): Call<JsonObject>

    @GET("authentication/token/new")
    fun createRequestToken(): Single<Response<JsonObject>>

    @POST("authentication/token/validate_with_login")
    fun login(@Body body: JsonObject): Single<Response<JsonObject>>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MovieListResponse>


    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int) : Call<JsonObject>
}