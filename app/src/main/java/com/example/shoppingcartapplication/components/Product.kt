package com.example.shoppingcartapplication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.shoppingcartapplication.R
import com.example.shoppingcartapplication.model.ProductDTO

@Composable
fun ProductCard(
    value: ProductDTO,
    onItemClick: (ProductDTO) -> Unit // Callback function to handle item click

) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val isLandscape = remember { screenWidth > screenHeight }

    val cardWidth = if (isLandscape) screenWidth / 3 else screenWidth / 2

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(8.dp)
            .width(cardWidth)
            .background(color = Color.White)
            .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoilImage(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                source = "https://i.postimg.cc/c1B00NGz/Lenovo-K3-Mini-Outdoor-Wireless-Speaker-1.png"
            )

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                ColorCard(value.taxPercentage.toString())
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = value.itemName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                RatingStar(4)
                Spacer(modifier = Modifier.height(8.dp))
                PriceRow(actualPrice = value.sellingPrice)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onItemClick(value) }) { // Pass the product DTO on click
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun PriceRow(actualPrice: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$$actualPrice",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.padding(end = 2.dp)
        )
    }
}

@Composable
fun ColorCard(text: String) {
    Box(
        modifier = Modifier
            .height(24.dp)
            .width(64.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFFFB7B4E)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tax $text%",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun RatingStar(rate: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        repeat(5) { index ->
            val isSelected = index < rate
            Image(
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
                    .padding(top = 4.dp),
                painter = if (isSelected) {
                    painterResource(id = R.drawable.vector__6_)
                } else {
                    painterResource(id = R.drawable.vector__4_)
                },
                contentDescription = "Star $index"
            )
        }
    }
}

@Composable
internal fun CoilImage(modifier: Modifier = Modifier, source: String) {
    SubcomposeAsyncImage(
        model = source,
        modifier = modifier,
        contentScale = ContentScale.Fit,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        contentDescription = "Product Image",
    )
}
