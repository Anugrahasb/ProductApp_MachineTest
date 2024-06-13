package com.example.shoppingcartapplication.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapplication.api.RESTResult
import com.example.shoppingcartapplication.dao.ProductEntity
import com.example.shoppingcartapplication.model.ProductDTO
import com.example.shoppingcartapplication.repository.HomePageRepository
import com.example.shoppingcartapplication.util.ShoppingCartHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: HomePageRepository) : ViewModel() {

    private val shoppingCartHelper = ShoppingCartHelper()

    // StateFlow to hold the UI state
    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState

    // MutableStateFlow to hold the current list of products from API
    private val _products = MutableStateFlow<List<ProductDTO>>(emptyList())
    val products: StateFlow<List<ProductDTO>> = _products

    // MutableState to hold the list of selected products
    var selectedItems by mutableStateOf(listOf<ProductDTO>())
    val subtotal: MutableState<Double> = mutableStateOf(0.0)
    val tax: MutableState<Double> = mutableStateOf(0.0)
    val total: MutableState<Double> = mutableStateOf(0.0)


    init {
        _uiState.value = UiState.Loading
        getHomePageData()
    }

    private fun getHomePageData() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getHomePageData()
            when (response) {
                is RESTResult.Success -> {
                    response.value?.let {
                        _products.value = response.value
                        _uiState.value = UiState.Loaded(value = response.value)
//                        saveProducts(it)
                    }
                }

                is RESTResult.Error -> {
                    _uiState.value = UiState.Error("Failed to fetch data")
                }
            }
        }
    }


    fun addToCart(item: ProductDTO) {
        viewModelScope.launch {
            selectedItems = selectedItems + item
            calculateCartSummary(selectedItems)
        }
    }

    private fun calculateCartSummary(cartItems: List<ProductDTO>) {
        subtotal.value = shoppingCartHelper.calculateSubtotal(cartItems)
        tax.value = shoppingCartHelper.calculateTax(cartItems)
        total.value = shoppingCartHelper.calculateTotal(cartItems)
    }

    fun clearCart() {
        selectedItems = emptyList()
        _uiState.value = UiState.CartUpdated
    }

    fun saveProducts(products: List<ProductDTO>) {
        val productEntities = products.map { product ->
            ProductEntity(
                itemID = product.itemID,
                itemName = product.itemName,
                sellingPrice = product.sellingPrice,
                taxPercentage = product.taxPercentage
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProduct(productEntities)
        }
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val products = repository.getAllProducts()
        }
    }

    fun clearProducts() {
        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteAllProducts(products)
        }
    }
}


class HomePageViewModelFactory(private val repository: HomePageRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class UiState {
    object Empty : UiState()
    object Loading : UiState()
    data class Loaded(val value: List<ProductDTO>?) : UiState()
    data class Error(val message: String) : UiState()
    object CartUpdated : UiState()
}
