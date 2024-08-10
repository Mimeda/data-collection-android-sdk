package com.mimeda.mlinkmobile.network.client

import com.mimeda.mlinkmobile.common.Resource
import com.mimeda.mlinkmobile.di.UtilModule
import com.mimeda.mlinkmobile.network.ErrorHandler
import com.mimeda.mlinkmobile.network.RequestHeader
import com.mimeda.mlinkmobile.network.model.ApiPostBody
import com.mimeda.mlinkmobile.network.model.BaseRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal abstract class ApiClient {
    protected val errorHandler = ErrorHandler()
    protected val networkScope = CoroutineScope(Dispatchers.IO)

    abstract suspend fun <R : Any> post(
        request: BaseRequest,
        endPoint: String,
        responseClass: Class<R>,
        header: RequestHeader
    ): Resource<R>

    abstract suspend fun <R : Any> get(endPoint: String, responseClass: Class<R>): Resource<R>

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