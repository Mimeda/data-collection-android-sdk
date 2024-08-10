package com.mimeda.mlinkmobile.network.client

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.gson.JsonSyntaxException
import com.mimeda.mlinkmobile.MlinkLogger
import com.mimeda.mlinkmobile.common.MlinkResource
import com.mimeda.mlinkmobile.common.withBaseUrl
import com.mimeda.mlinkmobile.di.MlinkUtilModule
import com.mimeda.mlinkmobile.network.MlinkError
import com.mimeda.mlinkmobile.network.MlinkRequestHeader
import com.mimeda.mlinkmobile.network.model.MlinkBaseRequest
import com.mimeda.mlinkmobile.network.model.MlinkBaseResponse
import kotlinx.coroutines.withContext

internal class MlinkFuelClient : MlinkApiClient() {

    override suspend fun <R : Any> post(
        request: MlinkBaseRequest,
        endPoint: String,
        responseClass: Class<R>,
        header: MlinkRequestHeader
    ): MlinkResource<R> = withContext(networkScope.coroutineContext) {
        sendPostRequest(request, endPoint.withBaseUrl(), responseClass, header)
    }

    override suspend fun <R : Any> get(
        endPoint: String,
        responseClass: Class<R>,
    ): MlinkResource<R> = withContext(networkScope.coroutineContext) {
        sendGetRequest(endPoint.withBaseUrl(), responseClass)
    }

    private fun <R : Any> sendGetRequest(
        url: String,
        responseClass: Class<R>,
    ): MlinkResource<R> = try {
        makeRequest(url.httpGet(), responseClass)
    } catch (e: Exception) {
        MlinkResource.Error(MlinkError.UnexpectedException(e.message))
    }

    private fun <R : Any> sendPostRequest(
        request: MlinkBaseRequest,
        url: String,
        responseClass: Class<R>,
        header: MlinkRequestHeader
    ): MlinkResource<R> {
        return try {
            val requestAsJson = MlinkUtilModule.jsonParser.toJson(request)
            val apiPostBodyJson = encryptRequest(request)
            MlinkLogger.printNetworkRequest(url, requestAsJson, header)
            makeRequest(
                url.httpPost().header(updateHeader(header)).jsonBody(apiPostBodyJson),
                responseClass
            )
        } catch (e: Exception) {
            val exception = when (e) {
                is JsonSyntaxException -> MlinkError.NotFoundNetworkException()
                else -> MlinkError.UnexpectedException(e.localizedMessage)
            }
            MlinkResource.Error(exception)
        }
    }

    private fun <R : Any> makeRequest(request: Request, responseClass: Class<R>): MlinkResource<R> {
        val response = request.response().second
        val jsonResponse = request.response().second.body().asString("application/json")
        val model = MlinkUtilModule.jsonParser.fromJson(jsonResponse, responseClass)
        return if (response.isSuccessful) {
            if (model == null) {
                MlinkLogger.printNetworkResponse(false, "Error", "Response is Null")
                MlinkResource.Error(MlinkError.ParseException())
            } else {
                MlinkLogger.printNetworkResponse(true, "Success", jsonResponse)
                MlinkResource.Success(model)
            }
        } else {
            val error = mlinkErrorHandler.getError(model as? MlinkBaseResponse<*>)
            MlinkLogger.printNetworkResponse(false, "Error", "${error.message} and response $jsonResponse")
            MlinkResource.Error(error)
        }
    }

    private fun updateHeader(
        header: MlinkRequestHeader? = null,
    ): Map<String, String> {
        if (header == null) return mapOf()
        val updatedHeader = mutableMapOf<String, String>()
        header.headerMap.forEach {
            updatedHeader[it.key] = it.value
        }
        return updatedHeader
    }
}