package com.mobile.coroutineapplication.presentation.movie_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.coroutineapplication.data.respository.MovieRepositoryImpl
import com.mobile.coroutineapplication.domain.models.Movie
import com.mobile.coroutineapplication.domain.repository.MovieRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieListViewModel : ViewModel() {

    val liveData = MutableLiveData<List<Movie>>()

    private val job = SupervisorJob()

    private val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private val movieRepository: MovieRepository= MovieRepositoryImpl()

    fun getMovies(page: Int = 1) {
        uiScope.launch {
            val list = withContext(coroutineContext) {
                movieRepository.getAllMovies(page)
            }
            liveData.postValue(list)
        }
    }
}