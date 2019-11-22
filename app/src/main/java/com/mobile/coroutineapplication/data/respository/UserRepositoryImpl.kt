package com.mobile.coroutineapplication.data.respository

import com.google.gson.JsonObject
import com.mobile.coroutineapplication.data.ApiClient
import com.mobile.coroutineapplication.data.MovieDBApi
import com.mobile.coroutineapplication.domain.repository.UserRepository
import io.reactivex.Single

class UserRepositoryImpl(
    private val movieDBApi: MovieDBApi
) : UserRepository {

    override fun login(username: String, password: String): Single<Boolean> {
        return movieDBApi.createRequestToken()
            .map { response ->
                response.body()?.getAsJsonPrimitive("request_token")?.asString
            }.flatMap { token ->
                val body = JsonObject().apply {
                    addProperty("username", username)
                    addProperty("password", password)
                    addProperty("request_token", token)
                }
                movieDBApi.login(body)
            }.map { loginResponse ->
                loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
            }
    }
}