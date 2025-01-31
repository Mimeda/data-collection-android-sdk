package com.mimeda.sdk.ui.productdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mimeda.sdk.data.model.Product
import com.mimeda.sdk.ui.productdetail.DetailContract.UiState

@Composable
fun DetailScreen(
    uiState: UiState,
) {
    uiState.product?.let { product ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier.size(96.dp),
                imageVector = Icons.Rounded.ShoppingCart,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = product.name,
                fontSize = 32.sp,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "$${product.price}",
                fontSize = 32.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        uiState = UiState(
            product = Product(
                id = 1,
                name = "Product Name",
                price = 9.99,
                barcode = 123456789,
                quantity = 1,
                description = "Product Description",
            )
        ),
    )
}