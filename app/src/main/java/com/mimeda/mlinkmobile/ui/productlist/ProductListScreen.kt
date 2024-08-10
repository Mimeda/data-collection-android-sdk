package com.mimeda.mlinkmobile.ui.productlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.mimeda.mlinkmobile.data.MockData
import com.mimeda.mlinkmobile.ui.productlist.ProductListContract.UiAction
import com.mimeda.mlinkmobile.ui.productlist.ProductListContract.UiEffect
import com.mimeda.mlinkmobile.ui.productlist.ProductListContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ProductListScreen(
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

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(uiState.products) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        onAction(UiAction.OnProductClick(it.id))
                    }
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Rounded.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                )
                Text(
                    text = it.name,
                    color = Color.White,
                    fontSize = 20.sp,
                )
                Text(
                    text = "$${it.price}",
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    ProductListScreen(
        uiState = UiState(
            products = MockData.products
        ),
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateToDetail = {}
    )
}