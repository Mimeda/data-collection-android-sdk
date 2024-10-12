package com.mimeda.mlinkmobile.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.mlinkmobile.MLink
import com.mimeda.mlinkmobile.data.MockData
import com.mimeda.mlinkmobile.data.model.MlinkEventProduct
import com.mimeda.mlinkmobile.data.model.Payload
import com.mimeda.mlinkmobile.data.model.Product
import com.mimeda.mlinkmobile.data.repository.MLinkEvents
import com.mimeda.mlinkmobile.ui.productlist.ProductListContract.UiAction
import com.mimeda.mlinkmobile.ui.productlist.ProductListContract.UiEffect
import com.mimeda.mlinkmobile.ui.productlist.ProductListContract.UiState
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
class ProductListViewModel @Inject constructor() : ViewModel() {

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
        val mLinkProducts = MockData.products.map {
            MlinkEventProduct(
                barcode = it.barcode,
                quantity = it.quantity,
                price = it.price,
            )
        }
        MLinkEvents.listingView(
            Payload(
                userId = 1,
                lineItems = MockData.products.map { it.id },
                products = mLinkProducts
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