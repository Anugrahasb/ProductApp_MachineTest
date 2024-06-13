package com.example.shoppingcartapplication.api

sealed class ErrorType {

    companion object {
        const val ERROR_SERVER = 503
        const val ERROR_CLIENT = 400
    }

    object Unauthorized : ErrorType()

    object NetworkUnavailable : ErrorType()


    data class ServerError(val code: Int, val message: String?) : ErrorType()

    data class ClientError(val code: Int, val message: String?) : ErrorType()
}