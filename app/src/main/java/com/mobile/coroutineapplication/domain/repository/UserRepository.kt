package com.mobile.coroutineapplication.domain.repository

import kotlinx.coroutines.Deferred

interface UserRepository {

    suspend fun login(username: String, password: String): Boolean
}