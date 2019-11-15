package com.mobile.coroutineapplication.data.respository

import com.google.gson.JsonObject
import com.mobile.coroutineapplication.data.ApiClient
import com.mobile.coroutineapplication.data.MovieDBApi
import com.mobile.coroutineapplication.domain.repository.UserRepository

class UserRepositoryImpl(
    private val movieDBApi: MovieDBApi
) : UserRepository {

    override suspend fun login(username: String, password: String): Boolean {
        val requestTokenResponse = movieDBApi.createRequestToken().await()
        val requestToken = requestTokenResponse
            .body()
            ?.getAsJsonPrimitive("request_token")
            ?.asString
        val body = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
            addProperty("request_token", requestToken)
        }
        val loginResponse = ApiClient.apiClient.login(body).await()
        return loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
    }
}