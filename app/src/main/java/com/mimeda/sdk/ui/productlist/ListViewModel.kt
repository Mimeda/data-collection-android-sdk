package com.mimeda.sdk.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.mlink.MlinkAds
import com.mimeda.mlink.MlinkEvents
import com.mimeda.mlink.data.MlinkAdPayload
import com.mimeda.mlink.data.MlinkEventPayload
import com.mimeda.sdk.data.MockData
import com.mimeda.sdk.data.model.Product
import com.mimeda.sdk.ui.productlist.ListContract.UiAction
import com.mimeda.sdk.ui.productlist.ListContract.UiEffect
import com.mimeda.sdk.ui.productlist.ListContract.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

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

            UiAction.SendAdImpression -> sendAdImpression()
            UiAction.OnAdClick -> sendAdClick()
        }
    }

    private fun getProducts() = viewModelScope.launch {
        _uiState.value = UiState(isLoading = true)
        val products = MockData.products
        _uiState.value = UiState(isLoading = false, products = products)

        val payload = MlinkEventPayload(
            userId = 17,
            categoryId = "1",
            lineItemIds = listOf(1, 2, 3),
        )
        MlinkEvents.Listing.view(payload)
    }

    private fun sendAdImpression() = viewModelScope.launch {
        val payload = MlinkAdPayload(
            userId = 1,
            productSku = "31231231",
            lineItemId = 1,
            creativeId = 1,
            adUnit = "list",
            keyword = "keyword",
            payload = "encrypted-data",
        )
        MlinkAds.impression(payload)
    }

    private fun sendAdClick() = viewModelScope.launch {
        val payload = MlinkAdPayload(
            userId = 1,
            productSku = "31231231",
            lineItemId = 1,
            creativeId = 1,
            adUnit = "list",
            keyword = "keyword",
            payload = "encrypted-data",
        )
        MlinkAds.click(payload)
    }
}

object ListContract {
    data class UiState(
        val isLoading: Boolean = false,
        val products: List<Product> = emptyList()
    )

    sealed class UiAction {
        data class OnProductClick(val productId: Int) : UiAction()
        data object SendAdImpression : UiAction()
        data object OnAdClick : UiAction()
    }

    sealed class UiEffect {
        data class GoToProductDetail(val productId: Int) : UiEffect()
    }
}