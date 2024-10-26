package com.mimeda.sdk.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.sdk.MlinkEvents
import com.mimeda.sdk.data.MlinkEventPayload
import com.mimeda.sdk.data.MlinkEventProduct
import com.mimeda.sdk.data.MockData
import com.mimeda.sdk.data.model.Product
import com.mimeda.sdk.ui.productdetail.ProductDetailContract.UiAction
import com.mimeda.sdk.ui.productdetail.ProductDetailContract.UiEffect
import com.mimeda.sdk.ui.productdetail.ProductDetailContract.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        savedStateHandle.get<Int>("productId")?.let { productId ->
            getProductDetail(productId)
        }
    }

    fun onAction(uiAction: UiAction) {

    }

    private fun getProductDetail(productId: Int) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        // Your service call here
        val product = MockData.products.find { it.id == productId }
        _uiState.update { it.copy(isLoading = false, product = product) }
        val mappedProduct = listOf(product).map {
            MlinkEventProduct(
                barcode = it?.barcode ?: 0,
                quantity = it?.quantity ?: 0,
                price = it?.price ?: 0.0,
            )
        }
        MlinkEvents.ProductDetails.view(
            MlinkEventPayload(
                userId = 123,
                adIDList = listOf(product?.barcode ?: 0),
                listOf(
                    MlinkEventProduct(
                        barcode = 1,
                        quantity = 1,
                        price = 100.0,
                    )
                )
            )
        )
    }
}

object ProductDetailContract {
    data class UiState(
        val isLoading: Boolean = false,
        val product: Product? = null,
    )

    sealed class UiAction {

    }

    sealed class UiEffect {

    }
}