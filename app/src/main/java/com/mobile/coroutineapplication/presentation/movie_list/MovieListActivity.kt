package com.mobile.coroutineapplication.presentation.movie_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mobile.coroutineapplication.R

class MovieListActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
        setData()
    }

    private fun setData() {
        viewModel.getMovies(page = 1)
        viewModel.liveData.observe(this, Observer { result ->
            Log.d("movies_list", result.toString())
        })
    }
}
