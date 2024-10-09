package com.mimeda.mlinkmobile.data.repository

import com.mimeda.mlinkmobile.data.model.Payload

interface MLinkRepository {
    suspend fun homeView(payload: Payload)
    suspend fun homeAddToCart(payload: Payload)
    suspend fun listingView(payload: Payload)
    suspend fun listingAddToCart(payload: Payload)
    suspend fun searchView(payload: Payload)
    suspend fun searchAddToCart(payload: Payload)
    suspend fun productDetailsView(payload: Payload)
    suspend fun productDetailsAddToCart(payload: Payload)
    suspend fun cartView(payload: Payload)
    suspend fun purchaseSuccess(payload: Payload)
}