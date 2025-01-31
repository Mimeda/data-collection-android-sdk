package com.mimeda.mlink.network

internal object MlinkClient {
    private val client by lazy { MlinkFuelClient() }
    suspend fun get(url: String, eventType: String) = client.get(url, eventType)
}