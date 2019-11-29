package com.mobile.coroutineapplication.presentation.di

import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mobile.coroutineapplication.data.ApiClient
import com.mobile.coroutineapplication.data.MovieDBApi
import com.mobile.coroutineapplication.data.respository.MovieRepositoryImpl
import com.mobile.coroutineapplication.data.respository.UserRepositoryImpl
import com.mobile.coroutineapplication.domain.repository.MovieRepository
import com.mobile.coroutineapplication.domain.repository.UserRepository
import com.mobile.coroutineapplication.presentation.LoginViewModel
import com.mobile.coroutineapplication.presentation.movie_list.MovieListViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

val networkModule = module {

    single(named("api_key")) { provideApiKey() }
    single(named("base_url")) { provideBaseUrl() }
    single { provideHttpLoggingInterceptor() }
    single { provideStethoInterceptor() }
    single { provideAuthInterceptor(apiKey = get(named("api_key"))) }
    single { provideOkHttp(
            loggingInterceptor = get(),
            stethoInterceptor = get(),
            authInterceptor = get()
        )
    }
    single { provideCallAdapterFactory() }
    single { provideConverterFactory() }
    single { provideRetrofit(
            baseUrl = get(named("base_url")),
            gsonConverterFactory = get(),
            callAdapterFactory = get(),
            okHttpClient = get()
        )
    }
    single { privideMovieDBApi(retrofit = get()) }
}

val repositoryModule = module {
    single { provideUserRepository(movieDBApi = get()) }
    single { provideMovieRepository(movieDBApi = get()) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(userRepository = get()) }
    viewModel { MovieListViewModel(movieRepository = get()) }
}


val appModule = listOf(networkModule, repositoryModule, viewModelModule)

// ------------------------------------Repository-------------------------------------------

fun provideMovieRepository(movieDBApi: MovieDBApi): MovieRepository = MovieRepositoryImpl(movieDBApi)

fun provideUserRepository(movieDBApi: MovieDBApi): UserRepository = UserRepositoryImpl(movieDBApi)


// ------------------------------------Network-------------------------------------------

fun provideApiKey(): String = ApiConstants.API_KEY

fun provideBaseUrl(): String = ApiConstants.BASE_URL

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor(
        HttpLoggingInterceptor.Logger { message -> Log.d("OkHttp", message) }
    ).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    return logging
}

fun provideStethoInterceptor(): StethoInterceptor = StethoInterceptor()

fun provideAuthInterceptor(apiKey: String): Interceptor {
    return Interceptor { chain ->
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()
        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }
}

fun provideOkHttp(
    loggingInterceptor: HttpLoggingInterceptor,
    stethoInterceptor: StethoInterceptor,
    authInterceptor: Interceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(stethoInterceptor)
        .build()
}

fun provideConverterFactory(): Converter.Factory = GsonConverterFactory.create()

fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

fun provideRetrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    gsonConverterFactory: Converter.Factory,
    callAdapterFactory: CallAdapter.Factory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(callAdapterFactory)
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()
}

fun privideMovieDBApi(retrofit: Retrofit): MovieDBApi = retrofit.create(MovieDBApi::class.java)

object ApiConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "72679493d1d20e8ef28849f2dd761c5a"
}

