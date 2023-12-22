package com.example.rickmorty.base

import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class BaseSourceImpl @Inject constructor() : BaseSource {

    override suspend fun <T> oneShotCalls(call: suspend () -> Response<T>): Results<T> {
        try {
            val response = call.invoke()
            return if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Results.Success(body)
                } else {
                    Results.Error(Exception("body is null"))
                }
            } else return Results.Error(Exception(""))
        } catch (e: Exception) {
            return when (e) {
                is SocketTimeoutException -> {
                    Results.Error(e)
                }

                is IOException -> {
                    Results.Error(e)
                }

                else -> {
                    Results.Error(e)
                }
            }
        }
    }

}
