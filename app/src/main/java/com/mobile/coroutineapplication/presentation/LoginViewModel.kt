package com.mobile.coroutineapplication.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.coroutineapplication.data.respository.UserRepositoryImpl
import com.mobile.coroutineapplication.domain.repository.UserRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val liveData = MutableLiveData<State>()

    private val compositeDisposable = CompositeDisposable()

    fun login(username: String, password: String) {
        Log.d("user_data", username + " ==== " + password)
        compositeDisposable.add(
            userRepository.login(username, password)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> liveData.value = State.ApiResult(result) },
                    { error -> liveData.value = State.Error(error.toString()) }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class State() {
        object ShowLoading: State()
        object HideLoading: State()
        data class ApiResult(val success: Boolean): State()
        data class Error(val error: String): State()
    }
}