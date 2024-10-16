package com.mimeda.sdk.network.client

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.mimeda.sdk.MLinkLogger
import kotlinx.coroutines.withContext

internal class MLinkFuelClient : MLinkApiClient() {

    override suspend fun get(url: String, eventName: String) =
        withContext(networkScope.coroutineContext) {
            sendGetRequest(url, eventName)
        }

    private fun sendGetRequest(url: String, eventName: String) = try {
        makeRequest(url.httpGet(), eventName)
    } catch (e: Exception) {
        MLinkLogger.error("Error: $e")
    }

    private fun makeRequest(request: Request, eventName: String) {
        val response = request.response().second
        if (response.isSuccessful) {
            MLinkLogger.debug("Success - $eventName")
        } else {
            MLinkLogger.error("Error:${response.statusCode} - $eventName")
        }
    }
}