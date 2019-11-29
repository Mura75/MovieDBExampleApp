package com.mobile.coroutineapplication.data.respository

import android.util.Log
import com.google.gson.JsonObject
import com.mobile.coroutineapplication.data.ApiClient
import com.mobile.coroutineapplication.data.MovieDBApi
import com.mobile.coroutineapplication.domain.repository.UserRepository
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepositoryImpl(
    private val movieDBApi: MovieDBApi
) : UserRepository {

    val TAG = "USER_REPOSITORY_IMPL"

    override fun login(username: String, password: String): Single<Boolean> {
        return Single.create(SingleOnSubscribe<String> { emitter ->
            movieDBApi.createRequestTokenCall().enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    emitter.onError(t)
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful) {
                        val token = response.body()?.getAsJsonPrimitive("request_token")?.asString
                        if (token != null) {
                            Log.d(TAG, "token exist: $token")
                            emitter.onSuccess(token)
                        } else {
                            Log.d(TAG, "token is null")
                            emitter.onError(Throwable("token is null"))
                        }
                    } else {
                        Log.d(TAG, "error body")
                        emitter.onError(Throwable(response.errorBody()?.toString()))
                    }
                }
            })
        })
            .flatMap { token ->
            val body = JsonObject().apply {
                addProperty("username", username)
                addProperty("password", password)
                addProperty("request_token", token)
            }
            movieDBApi.login(body)
        }.map { loginResponse ->
            loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
        }

//        return movieDBApi.createRequestToken()
//            .map { response ->
//                response.body()?.getAsJsonPrimitive("request_token")?.asString
//            }.flatMap { token ->
//                val body = JsonObject().apply {
//                    addProperty("username", username)
//                    addProperty("password", password)
//                    addProperty("request_token", token)
//                }
//                movieDBApi.login(body)
//            }.map { loginResponse ->
//                loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
//            }
    }
}