package com.example.shoppingcartapplication.repository

import android.content.Context
import com.example.shoppingcartapplication.api.ApiService
import com.example.shoppingcartapplication.api.RESTResult
import com.example.shoppingcartapplication.api.customTryRESTCall
import com.example.shoppingcartapplication.dao.ProductDao
import com.example.shoppingcartapplication.dao.ProductEntity
import com.example.shoppingcartapplication.model.ProductDTO
import kotlinx.coroutines.flow.Flow


class HomePageRepositoryImpl(
    private val context: Context,
    private val apiService: ApiService,
    private val  productDao: ProductDao
): HomePageRepository {

   override suspend fun getHomePageData(): RESTResult<List<ProductDTO>> {
        return customTryRESTCall {
            apiService.getItems().let {

                it
            }
        }
    }
    override suspend fun insertProduct(product: List<ProductEntity>) {
       productDao.insertProduct(product)
    }

    override fun getAllProducts(): Flow<List<ProductEntity>> {
       return  productDao.getAllProducts()
    }

    override suspend fun deleteAllProducts(product: ProductEntity) {
        productDao.getAllProducts()
    }
}

