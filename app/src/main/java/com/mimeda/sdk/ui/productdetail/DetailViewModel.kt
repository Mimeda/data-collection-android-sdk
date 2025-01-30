package com.mimeda.sdk.ui.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.mlink.MlinkEvents
import com.mimeda.mlink.data.MlinkEventPayload
import com.mimeda.sdk.data.MockData
import com.mimeda.sdk.data.model.Product
import com.mimeda.sdk.ui.productdetail.DetailContract.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<Int>("productId")?.let { productId ->
            getProductDetail(productId)
        }
    }

    private fun getProductDetail(productId: Int) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        // Your service call here
        val product = MockData.products.find { it.id == productId }
        _uiState.update { it.copy(isLoading = false, product = product) }
        MlinkEvents.ProductDetails.view(
            MlinkEventPayload(
                userId = 123,
            )
        )
    }
}

object DetailContract {
    data class UiState(
        val isLoading: Boolean = false,
        val product: Product? = null,
    )
}