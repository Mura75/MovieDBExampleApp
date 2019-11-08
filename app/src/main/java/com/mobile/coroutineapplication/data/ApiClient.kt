package com.mobile.coroutineapplication.data

import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_KEY = "72679493d1d20e8ef28849f2dd761c5a"


    //Lazy initialization
    val apiClient: MovieDBApi by lazy {
        val logging = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message -> Log.d("OkHttp", message)}
        ).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttp = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val newUrl = chain.request().url()
                    .newBuilder()
                    .addQueryParameter("api_key",
                        API_KEY
                    )
                    .build()
                val newRequest = chain.request()
                    .newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            }
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        return@lazy retrofit.create(MovieDBApi::class.java)
    }



}

