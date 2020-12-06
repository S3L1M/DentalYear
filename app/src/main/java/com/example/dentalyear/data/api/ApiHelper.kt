package com.example.dentalyear.data.api

import com.example.dentalyear.utils.Resource
import retrofit2.Response

abstract class ApiHelper {
    suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful)
            {
                if (response.body() != null) return Resource.success(response.body())
            }
            // Data is empty
            return Resource.error(null, response.message())
        } catch (cause: Throwable) {
            return Resource.error(null, cause.message.toString())
        }
    }
}