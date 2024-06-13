package com.example.shoppingcartapplication.api

import retrofit2.Response

suspend fun <T : Any>  customTryRESTCall(call: suspend () -> Response<T>): RESTResult<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            RESTResult.Success(response.body())
        } else {
            val wrapper = Wrapper.from(response)
            val code = (wrapper?.metaData?.get("code") as? String)?.toIntOrNull() ?: response.code()
            RESTResult.Error(ErrorType.ServerError(code, wrapper?.messages?.firstOrNull() ?: response.message()), wrapper?.rawData)
        }
    } catch (e: Exception) {
        e.toRESTError()
    }
}