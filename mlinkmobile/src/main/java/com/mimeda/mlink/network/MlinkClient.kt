package com.mimeda.mlink.network

internal object MlinkClient {
    /**
     * Lazily initialized instance of MlinkFuelClient
     */
    private val client by lazy { MlinkFuelClient() }
    /**
     * Performs a GET request using the MlinkFuelClient
     */
    suspend fun get(url: String, eventType: String) = client.get(url, eventType)
}