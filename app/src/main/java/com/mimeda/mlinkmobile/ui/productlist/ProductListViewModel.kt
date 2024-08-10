package com.mimeda.mlinkmobile.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mimeda.mlinkmobile.data.MockData
import com.mimeda.mlinkmobile.data.model.Product
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
        _uiState.update {
            it.copy(
                products = MockData.products
            )
        }
    }

    fun onAction(uiAction: UiAction) = viewModelScope.launch {
        when (uiAction) {
            is UiAction.OnProductClick -> {
                _uiEffect.send(UiEffect.GoToProductDetail(uiAction.productId))
            }
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