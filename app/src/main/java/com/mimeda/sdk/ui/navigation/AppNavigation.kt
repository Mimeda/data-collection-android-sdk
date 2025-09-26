package com.mimeda.sdk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mimeda.sdk.ui.productdetail.DetailScreen
import com.mimeda.sdk.ui.productdetail.DetailViewModel
import com.mimeda.sdk.ui.productlist.ListScreen
import com.mimeda.sdk.ui.productlist.ListViewModel

@Composable
fun ProductAppNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = "list"
    ) {
        composable("list") {
            val viewModel: ListViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            ListScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction,
                onNavigateToDetail = { productId ->
                    navHostController.navigate("product_detail/$productId")
                }
            )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) {
            val viewModel: DetailViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            DetailScreen(
                uiState = uiState,
            )
        }
    }
}