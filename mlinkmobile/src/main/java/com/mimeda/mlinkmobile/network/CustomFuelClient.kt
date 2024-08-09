package com.mimeda.mlinkmobile.network

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.gson.JsonSyntaxException
import com.mimeda.mlinkmobile.Logger
import com.mimeda.mlinkmobile.common.withBaseUrl
import kotlinx.coroutines.withContext

internal class CustomFuelClient : ApiClient() {

    override suspend fun <R : Any> post(
        request: BaseRequest,
        endPoint: Endpoint,
        responseClass: Class<R>,
        header: RequestHeader
    ): Resource<R> = withContext(networkScope.coroutineContext) {
        sendPostRequest(request, endPoint.path.withBaseUrl(), responseClass, header)
    }

    override suspend fun <R : Any> get(
        endPoint: Endpoint,
        responseClass: Class<R>,
    ): Resource<R> = withContext(networkScope.coroutineContext) {
        sendGetRequest(endPoint.path.withBaseUrl(), responseClass)
    }

    private fun <R : Any> sendGetRequest(
        url: String,
        responseClass: Class<R>,
    ): Resource<R> = try {
        makeRequest(url.httpGet(), responseClass)
    } catch (e: Exception) {
        Resource.Error(MlinkError.UnexpectedException(e.message))
    }

    private fun <R : Any> sendPostRequest(
        request: BaseRequest,
        url: String,
        responseClass: Class<R>,
        header: RequestHeader
    ): Resource<R> {
        return try {
            val requestAsJson = UtilModule.jsonParser.toJson(request)
            val apiPostBodyJson = encryptRequest(request)
            Logger.printNetworkRequest(url, requestAsJson, header)
            makeRequest(
                url.httpPost()
                    .header(updateHeader(header))
                    .jsonBody(apiPostBodyJson),
                responseClass
            )
        } catch (e: Exception) {
            val exception = when (e) {
                is JsonSyntaxException -> MlinkError.NotFoundNetworkException()
                else -> MlinkError.UnexpectedException(e.localizedMessage)
            }
            Resource.Error(exception)
        }
    }

    private fun <R : Any> makeRequest(request: Request, responseClass: Class<R>): Resource<R> {
        val response = request.response().second
        val jsonResponse = request.response().second.body().asString("application/json")
        val model = UtilModule.jsonParser.fromJson(jsonResponse, responseClass)
        return if (response.isSuccessful) {
            if (model == null) {
                Logger.printNetworkResponse(false, "Error: ", "Response is Null")
                Resource.Error(MlinkError.ParseException())
            } else {
                Logger.printNetworkResponse(true, "Success", jsonResponse)
                Resource.Success(model)
            }
        } else {
            val error = errorHandler.getError(model as? BaseResponse<*>)
            Logger.printNetworkResponse(false, "Success", "${error.message} and response $jsonResponse")
            Resource.Error(error)
        }
    }

    private fun updateHeader(
        header: RequestHeader? = null,
    ): Map<String, String> {
        if (header == null) return mapOf()
        val updatedHeader = mutableMapOf<String, String>()
        header.headerMap.forEach {
            updatedHeader[it.key.key] = it.value
        }
        return updatedHeader
    }
}