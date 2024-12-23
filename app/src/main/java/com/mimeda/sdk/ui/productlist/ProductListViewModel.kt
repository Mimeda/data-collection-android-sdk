package com.mimeda.sdk.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.mlink.MlinkAds
import com.mimeda.mlink.MlinkEvents
import com.mimeda.mlink.data.MlinkAdPayload
import com.mimeda.mlink.data.MlinkEventPayload
import com.mimeda.mlink.data.MlinkEventProduct
import com.mimeda.sdk.data.model.Product
import com.mimeda.sdk.ui.productlist.ProductListContract.UiAction
import com.mimeda.sdk.ui.productlist.ProductListContract.UiEffect
import com.mimeda.sdk.ui.productlist.ProductListContract.UiState
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
                categoryId = "123",
                products = listOf(
                    MlinkEventProduct(
                        barcode = 123,
                        quantity = 1,
                        price = 100.0
                    ),
                    MlinkEventProduct(
                        barcode = 456,
                        quantity = 2,
                        price = 200.0
                    )
                ),
                transactionId = 123,
                totalRowCount = 2,
            )
            MlinkEvents.Listing.view(payload)
            MlinkAds.impression(
                MlinkAdPayload(
                    lineItemId = 123,
                    creativeId = 123,
                    adUnit = "ad_unit",
                    keyword = "keyword",
                )
            )
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