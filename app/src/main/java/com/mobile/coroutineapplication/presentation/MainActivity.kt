package com.mobile.coroutineapplication.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mobile.coroutineapplication.data.ApiClient
import com.mobile.coroutineapplication.R
import com.mobile.coroutineapplication.presentation.movie_list.MovieListActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.await
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private val job = SupervisorJob()

    private val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        editTextUserName.setText("Mura")
        editTextPassword.setText("L6zwZSPZmB6EfZr")

        buttonLogin.setOnClickListener {
            loginViewModel.login(
                username = editTextUserName.text.toString(),
                password = editTextPassword.text.toString()
            )
        }

        setData()
    }

    private fun setData() {
        loginViewModel.liveData.observe(this, Observer { result ->
            when(result) {
                is LoginViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is LoginViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is LoginViewModel.State.ApiResult -> {
                    Log.d("activity_result", result.success.toString())
                    if (result.success) {
                        startActivity(Intent(this, MovieListActivity::class.java))
                        Toast.makeText(this, "Login is success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Login is wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                is LoginViewModel.State.Error -> {

                }
            }
        })
    }

    private fun moviesRequest() {
        uiScope.launch {
            val result = ApiClient.apiClient.getPopularMovies(page = 1).await()
//            val array = success.getAsJsonArray("results")
//            val id = array.get(0).asJsonObject.getAsJsonPrimitive("id").asInt
//            val movieInfo = ApiClient.apiClient.getMovie(movieId = id).await()
            Log.d("result_data", result.movies.toString())
            val movieInfo = ApiClient.apiClient.getMovie(movieId = result.movies.get(0).id).await()
            Log.d("result_data_detail", movieInfo.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
