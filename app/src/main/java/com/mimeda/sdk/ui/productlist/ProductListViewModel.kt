package com.mimeda.sdk.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.sdk.MlinkEvents
import com.mimeda.sdk.data.MlinkEventPayload
import com.mimeda.sdk.data.MlinkEventProduct
import com.mimeda.sdk.data.model.Product
import com.mimeda.sdk.ui.productlist.ProductListContract.UiEffect
import com.mimeda.sdk.ui.productlist.ProductListContract.UiState
import com.mimeda.sdk.ui.productlist.ProductListContract.UiAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        getProducts()
    }

    fun onAction(uiAction: UiAction) = viewModelScope.launch {
        when (uiAction) {
            is UiAction.OnProductClick -> {
                _uiEffect.send(UiEffect.GoToProductDetail(uiAction.productId))
            }
        }
    }

    private fun getProducts() {
        viewModelScope.launch {
            // Your service call here
            val payload = MlinkEventPayload(
                userId = 0,
                adIDList = listOf(1, 2, 3),
                products = listOf(
                    MlinkEventProduct(
                        barcode = 1,
                        quantity = 1,
                        price = 100.0,
                    ),
                )
            )
            MlinkEvents.Listing.view(payload)
        }
    }
}

object ProductListContract {
    data class UiState(
        val isLoading: Boolean = false,
        val products: List<Product> = emptyList()
    )

    sealed class UiAction {
        data class OnProductClick(val productId: Int) : UiAction()
    }

    sealed class UiEffect {
        data class GoToProductDetail(val productId: Int) : UiEffect()
    }
}