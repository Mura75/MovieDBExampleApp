package com.mobile.coroutineapplication.presentation.movie_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.coroutineapplication.data.respository.MovieRepositoryImpl
import com.mobile.coroutineapplication.domain.models.Movie
import com.mobile.coroutineapplication.domain.repository.MovieRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val liveData = MutableLiveData<List<Movie>>()

    private val compositeDisposable = CompositeDisposable()

    fun getMovies(page: Int = 1) {
        compositeDisposable.add(
            movieRepository.getAllMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> liveData.postValue(result) },
                    { error -> }
                )
        )
    }
}