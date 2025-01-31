package com.mimeda.sdk.ui.productlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.mimeda.sdk.data.MockData
import com.mimeda.sdk.data.model.Product
import com.mimeda.sdk.ui.productlist.ListContract.UiAction
import com.mimeda.sdk.ui.productlist.ListContract.UiEffect
import com.mimeda.sdk.ui.productlist.ListContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ListScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is UiEffect.GoToProductDetail -> onNavigateToDetail(effect.productId)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        onAction(UiAction.SendAdImpression)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Black, shape = RoundedCornerShape(16.dp)
                )
                .clickable { onAction(UiAction.OnAdClick) }
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Ad View",
                fontSize = 24.sp,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Product List",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(uiState.products) { product ->
                ProductItem(
                    product = product,
                    onProductClick = { onAction(UiAction.OnProductClick(it)) },
                )
            }
        }
    }
}

@Composable
private fun ProductItem(
    product: Product,
    onProductClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Black, shape = RoundedCornerShape(16.dp)
            )
            .clickable { onProductClick(product.id) }
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = Color.White,
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column {
            Text(
                text = product.name,
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$${product.price}",
                color = Color.White,
                fontSize = 20.sp,
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    ListScreen(uiState = UiState(
        isLoading = false,
        products = MockData.products,
    ), uiEffect = emptyFlow(), onAction = {}, onNavigateToDetail = {})
}