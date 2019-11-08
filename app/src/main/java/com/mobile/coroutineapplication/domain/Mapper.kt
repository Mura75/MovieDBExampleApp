package com.mobile.coroutineapplication.domain

interface Mapper<M, N> {

    fun from(item: M): N

    fun to(item: N): M
}