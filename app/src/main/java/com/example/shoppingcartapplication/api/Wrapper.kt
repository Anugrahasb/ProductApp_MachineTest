package com.example.shoppingcartapplication.api

import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response

data class Wrapper<T>(
    var messages: List<String>? = null,
    var data: T? = null,
    @JvmSuppressWildcards var metaData: Map<String, Any?>? = null,
    var rawData: String? = null
) {

    data class WrapperErased(val messages: List<String>?, val metaData: Map<String, Any?>?, val data: Any?)

    companion object {

       private val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            .adapter(WrapperErased::class.java)

        fun <T : Any> from(rawResponse: Response<T>?): Wrapper<Nothing>? {
            val body = rawResponse?.errorBody()?.string() ?: return null
            val erased = adapter.fromJson(body) ?: return null
            return Wrapper(erased.messages, null, erased.metaData, rawData = Gson().toJson(erased.data))
        }
    }
}