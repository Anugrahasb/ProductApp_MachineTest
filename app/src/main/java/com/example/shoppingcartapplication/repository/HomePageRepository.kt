package com.example.shoppingcartapplication.repository

import com.example.shoppingcartapplication.api.RESTResult
import com.example.shoppingcartapplication.dao.ProductEntity
import com.example.shoppingcartapplication.model.ProductDTO
import kotlinx.coroutines.flow.Flow

interface HomePageRepository {
    suspend fun insertProduct(product: List<ProductEntity>)
    fun getAllProducts(): Flow<List<ProductEntity>>
    suspend fun deleteAllProducts(product: ProductEntity)
    suspend fun getHomePageData(): RESTResult<List<ProductDTO>>
}