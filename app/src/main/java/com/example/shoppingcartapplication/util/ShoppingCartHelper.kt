package com.example.shoppingcartapplication.util

import com.example.shoppingcartapplication.model.ProductDTO

class ShoppingCartHelper {

    fun calculateSubtotal(cartItems: List<ProductDTO>): Double {
        return cartItems.sumOf { it.sellingPrice }
    }

    fun calculateTax(cartItems: List<ProductDTO>): Double {
        return cartItems.sumOf { it.sellingPrice * it.taxPercentage / 100 }
    }

    fun calculateTotal(cartItems: List<ProductDTO>): Double {
        val subtotal = calculateSubtotal(cartItems)
        val tax = calculateTax(cartItems)
        return subtotal + tax
    }
}
