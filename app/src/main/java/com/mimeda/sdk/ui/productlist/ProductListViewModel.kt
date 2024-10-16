package com.mimeda.sdk.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.sdk.data.MockData
import com.mimeda.sdk.data.model.MlinkEventProduct
import com.mimeda.sdk.data.model.Payload
import com.mimeda.sdk.data.model.Product
import com.mimeda.sdk.data.repository.MLinkEvents
import com.mimeda.sdk.ui.productlist.ProductListContract.UiAction
import com.mimeda.sdk.ui.productlist.ProductListContract.UiEffect
import com.mimeda.sdk.ui.productlist.ProductListContract.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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

    private fun getProducts() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        // Your service call here
        _uiState.update { it.copy(isLoading = false, products = MockData.products) }
        MLinkEvents.listingView(
            Payload(
                userId = 123,
                lineItems = listOf(1, 2, 3),
                products = listOf(
                    MlinkEventProduct(
                        barcode = 1,
                        quantity = 1,
                        price = 100.0
                    ),
                )
            )
        )
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