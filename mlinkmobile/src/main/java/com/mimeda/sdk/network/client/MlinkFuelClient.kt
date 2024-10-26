package com.mimeda.sdk.network.client

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.mimeda.sdk.MlinkLogger
import kotlinx.coroutines.withContext

internal class MlinkFuelClient : MlinkApiClient() {

    override suspend fun get(url: String, eventName: String) =
        withContext(networkScope.coroutineContext) {
            sendGetRequest(url, eventName)
        }

    private fun sendGetRequest(url: String, eventName: String) = try {
        makeRequest(url.httpGet(), eventName)
    } catch (e: Exception) {
        MlinkLogger.error("Mlink: Error: $e")
    }

    private fun makeRequest(request: Request, eventName: String) {
        val response = request.response().second
        if (response.isSuccessful) {
            MlinkLogger.debug("Mlink: Success - $eventName")
        } else {
            MlinkLogger.error("Mlink: Error:${response.statusCode} - $eventName")
        }
    }
}