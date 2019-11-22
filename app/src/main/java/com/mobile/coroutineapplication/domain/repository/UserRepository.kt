package com.mobile.coroutineapplication.domain.repository

import io.reactivex.Single
import kotlinx.coroutines.Deferred

interface UserRepository {

    fun login(username: String, password: String): Single<Boolean>
}