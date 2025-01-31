package com.mimeda.mlink.network

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.mimeda.mlink.MlinkLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class MlinkFuelClient {

    suspend fun get(url: String, eventName: String) = withContext(Dispatchers.IO) {
        try {
            val request = url.httpGet()
            executeRequest(request, eventName)
        } catch (e: Exception) {
            MlinkLogger.error("Mlink: Error: $e - $eventName")
        }
    }

    private fun executeRequest(request: Request, eventName: String) {
        val (_, response) = request.response()

        if (response.isSuccessful) {
            MlinkLogger.debug("Mlink: Success - $eventName")
        } else {
            MlinkLogger.error("Mlink: Error ${response.statusCode} - $eventName")
        }
    }
}