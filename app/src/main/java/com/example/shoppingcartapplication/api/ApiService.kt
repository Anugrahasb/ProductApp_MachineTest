package com.example.shoppingcartapplication.api

import com.example.shoppingcartapplication.model.ProductDTO
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("Todo")
    suspend fun getItems(): Response<List<ProductDTO>>
}
