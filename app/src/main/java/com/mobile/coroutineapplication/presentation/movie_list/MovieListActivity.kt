package com.mobile.coroutineapplication.presentation.movie_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mobile.coroutineapplication.R
import org.koin.android.ext.android.inject

class MovieListActivity : AppCompatActivity() {

    private val viewModel: MovieListViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setData()
    }

    private fun setData() {
        viewModel.getMovies(page = 1)
        viewModel.liveData.observe(this, Observer { result ->
            Log.d("movies_list", result.toString())
        })
    }
}
