package com.mimeda.mlinkmobile.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.mlinkmobile.MLink
import com.mimeda.mlinkmobile.data.MockData
import com.mimeda.mlinkmobile.data.model.Payload
import com.mimeda.mlinkmobile.data.model.PayloadProduct
import com.mimeda.mlinkmobile.data.model.Product
import com.mimeda.mlinkmobile.ui.productdetail.ProductDetailContract.UiAction
import com.mimeda.mlinkmobile.ui.productdetail.ProductDetailContract.UiEffect
import com.mimeda.mlinkmobile.ui.productdetail.ProductDetailContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
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
            PayloadProduct(
                barcode = it?.barcode ?: 0,
                quantity = it?.quantity ?: 0,
                price = it?.price ?: 0.0,
            )
        }
        MLink.Publisher.Events.ProductDetails.view(Payload(1, listOf(product?.barcode ?: 0), mappedProduct))
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