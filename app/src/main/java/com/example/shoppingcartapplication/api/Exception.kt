package com.example.shoppingcartapplication.api

import java.io.IOException
import java.net.UnknownHostException

class NoNetworkException : IOException()

class UnauthorizedException : IOException()

class ClientException : IOException()

fun <T : Any> Exception.toRESTError(): RESTResult.Error<T> {
    when (this) {
        is UnknownHostException, is NoNetworkException -> return RESTResult.Error(ErrorType.NetworkUnavailable, null)
        is UnauthorizedException -> return RESTResult.Error(ErrorType.Unauthorized, null)
        is ClientException -> return RESTResult.Error(
            ErrorType.ClientError(
                ErrorType.ERROR_CLIENT,
                localizedMessage
            ), null
        )
    }
    return RESTResult.Error(ErrorType.ServerError(ErrorType.ERROR_SERVER, localizedMessage), null)
}