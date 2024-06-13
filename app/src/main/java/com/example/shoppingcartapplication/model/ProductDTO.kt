package com.example.shoppingcartapplication.model

data class ProductDTO(
    val itemID: String,
    val itemName: String,
    val sellingPrice: Double,
    val taxPercentage: Double
)

