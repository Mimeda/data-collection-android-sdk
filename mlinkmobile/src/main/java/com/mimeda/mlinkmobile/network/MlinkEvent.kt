package com.mimeda.mlinkmobile.network

interface MlinkEvent {
    suspend fun sendSampleEvent(productId: Int)
}