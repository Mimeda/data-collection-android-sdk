package com.mimeda.mlinkmobile.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal abstract class ApiClient {
    protected val errorHandler = ErrorHandler()
    protected val networkScope = CoroutineScope(Dispatchers.IO)

    abstract suspend fun <R : Any> post(
        request: BaseRequest,
        endPoint: Endpoint,
        responseClass: Class<R>,
        header: RequestHeader
    ): Resource<R>

    abstract suspend fun <R : Any> get(endPoint: Endpoint, responseClass: Class<R>): Resource<R>

    protected fun encryptRequest(request: BaseRequest): String {
        val jsonEncrypted = UtilModule.jsonParser.toJson(request)
        val apiPostBody = ApiPostBody(jsonEncrypted)
        return UtilModule.jsonParser.toJson(apiPostBody)
    }

    protected fun decryptResponse(
        isEncrypted: Boolean,
        responseString: String?
    ): String {
        return when {
            responseString.isNullOrEmpty() -> ""
            isEncrypted -> decrypt(responseString)
            else -> responseString
        }
    }

    private fun decrypt(response: String): String {
        val json = response.replace("\\/", "/")
        val postBody = UtilModule.jsonParser
            .fromJson(json, ApiPostBody::class.java)
        return postBody?.data.orEmpty()
    }
}