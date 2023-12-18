package com.example.rickmorty.base

import retrofit2.Response

interface BaseSource {
    suspend fun <T> oneShotCalls(call: suspend () -> Response<T>): Results<T>
}
