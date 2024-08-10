package com.mimeda.mlinkmobile.network.client

import com.mimeda.mlinkmobile.common.MlinkResource
import com.mimeda.mlinkmobile.di.MlinkUtilModule
import com.mimeda.mlinkmobile.network.MlinkErrorHandler
import com.mimeda.mlinkmobile.network.model.MlinkRequestHeader
import com.mimeda.mlinkmobile.network.model.MlinkApiPostBody
import com.mimeda.mlinkmobile.network.model.MlinkBaseRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class MlinkApiClient {
    protected val mlinkErrorHandler = MlinkErrorHandler()
    protected val networkScope = CoroutineScope(Dispatchers.IO)

    abstract suspend fun <R : Any> post(
        request: MlinkBaseRequest,
        endPoint: String,
        responseClass: Class<R>,
        header: MlinkRequestHeader
    ): MlinkResource<R>

    abstract suspend fun <R : Any> get(endPoint: String, responseClass: Class<R>): MlinkResource<R>

    protected fun encryptRequest(request: MlinkBaseRequest): String {
        val jsonEncrypted = MlinkUtilModule.jsonParser.toJson(request)
        val mlinkApiPostBody = MlinkApiPostBody(jsonEncrypted)
        return MlinkUtilModule.jsonParser.toJson(mlinkApiPostBody)
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
        val postBody = MlinkUtilModule.jsonParser
            .fromJson(json, MlinkApiPostBody::class.java)
        return postBody?.data.orEmpty()
    }
}