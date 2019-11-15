package com.mobile.coroutineapplication.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.coroutineapplication.data.respository.UserRepositoryImpl
import com.mobile.coroutineapplication.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val liveData = MutableLiveData<State>()

    private val job = SupervisorJob()

    private val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    fun login(username: String, password: String) {
        uiScope.launch {
            liveData.value = State.ShowLoading
            val result = withContext(Dispatchers.IO) {
                userRepository.login(username, password)
            }
            liveData.value = State.HideLoading
            liveData.postValue(State.ApiResult(result))
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    sealed class State() {
        object ShowLoading: State()
        object HideLoading: State()
        data class ApiResult(val success: Boolean): State()
        data class Error(val error: String): State()
    }
}