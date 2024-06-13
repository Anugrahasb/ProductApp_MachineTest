package com.example.shoppingcartapplication.api

sealed class RESTResult<T> {

    data class Error<T>(val type: ErrorType, val rawData:String?) : RESTResult<T>()

    data class Success<T>(val value: T?) : RESTResult<T>()

}