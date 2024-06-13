package com.example.shoppingcartapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingcartapplication.api.RetrofitInstance
import com.example.shoppingcartapplication.components.CircularProgressIndicators
import com.example.shoppingcartapplication.components.ProductCard
import com.example.shoppingcartapplication.components.TopAppBarSection
import com.example.shoppingcartapplication.dao.ProductDatabase
import com.example.shoppingcartapplication.model.ProductDTO
import com.example.shoppingcartapplication.repository.HomePageRepository
import com.example.shoppingcartapplication.repository.HomePageRepositoryImpl
import com.example.shoppingcartapplication.ui.theme.ShoppingCartApplicationTheme
import com.example.shoppingcartapplication.viewmodel.HomePageViewModelFactory
import com.example.shoppingcartapplication.viewmodel.ItemViewModel
import com.example.shoppingcartapplication.viewmodel.UiState

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Manual Dao Creation
        val dao = ProductDatabase.getDatabase(application).productDao()

        // 1. Manual ViewModel Creation
        val repository = HomePageRepositoryImpl(applicationContext, RetrofitInstance.api,dao)
        val viewModelFactory = HomePageViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ItemViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            ShoppingCartApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        when (val state = viewModel.uiState.collectAsState().value) {
                            is UiState.Empty -> {
                            }

                            is UiState.Loading -> {
                                CircularProgressIndicators()
                            }

                            is UiState.Loaded -> {
                                state.value?.let {
                                    NavGraph(value = it, viewModel = viewModel)
                                }
                            }

                            is UiState.Error -> {

                            }

                            UiState.CartUpdated -> {}
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NavGraph(
    startDestination: String = "item_list",
    value: List<ProductDTO>,
    viewModel: ItemViewModel
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = startDestination) {
        composable("item_list") {
            ItemListScreen(
                navController, items = value, viewModel = viewModel
            )
        }
        composable("cart") {
            CartScreen(
                items = viewModel.selectedItems,
                tax = viewModel.tax.value,
                total = viewModel.total.value,
                subtotal = viewModel.subtotal.value,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartScreen(
    items: List<ProductDTO>,
    onBackClick: () -> Unit,
    tax: Double,
    total: Double,
    subtotal: Double,
) {
    Scaffold(
        topBar = {
            TopAppBarSection(
                title = "Cart Product",
                showBackButton = true,
                onBackClick = { onBackClick.invoke() }
            )
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    items(items) { item ->
                        ProductCard(item) {
                            // Handle any actions related to items in the cart screen
                        }
                    }
                }
                CartSummary(
                    tax = tax,
                    total = total,
                    subtotal = subtotal,
                )
            }
        }
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ItemListScreen(
    navController: NavController,
    items: List<ProductDTO>,
    viewModel: ItemViewModel
) {
    Scaffold(
        topBar = {
            TopAppBarSection()
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    items(items) { item ->
                        ProductCard(item) {
                            viewModel.addToCart(it) // Add to cart action
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("cart") }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "View Cart")
            }
        }
    )
}

@Composable
private fun CartSummary(
    tax: Double,
    total: Double,
    subtotal: Double,
) {
    Card {
        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFFB7B4E)),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = "Total ${total}%",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.White
                )
                Text(
                    text = "Tax ${tax}%",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.White
                )
                Text(
                    text = "Subtotal ${subtotal}%",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}
