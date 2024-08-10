package com.mimeda.mlinkmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mimeda.mlinkmobile.ui.productdetail.ProductDetailScreen
import com.mimeda.mlinkmobile.ui.productdetail.ProductDetailViewModel
import com.mimeda.mlinkmobile.ui.productlist.ProductListScreen
import com.mimeda.mlinkmobile.ui.productlist.ProductListViewModel

@Composable
fun ProductAppNavigation(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = "product_list"
    ) {
        composable("product_list") {
            val viewModel: ProductListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            ProductListScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateToDetail = { productId ->
                    navHostController.navigate("product_detail?productId=$productId")
                }
            )
        }

        composable(
            route = "product_detail?productId={productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) {
            val viewModel: ProductDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            ProductDetailScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
            )
        }
    }
}