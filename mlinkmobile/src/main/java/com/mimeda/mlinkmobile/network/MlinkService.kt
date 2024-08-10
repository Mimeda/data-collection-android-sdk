package com.mimeda.mlinkmobile.network

import com.mimeda.mlinkmobile.BuildConfig
import com.mimeda.mlinkmobile.MlinkManager
import com.mimeda.mlinkmobile.common.Constants
import com.mimeda.mlinkmobile.network.client.ApiClient
import com.mimeda.mlinkmobile.network.model.SampleRequest
import com.mimeda.mlinkmobile.network.model.SampleResponse

internal class MlinkService(
    private val apiClient: ApiClient,
) {
    suspend fun sampleRequest(productId: Int, productName: String) {
        apiClient.post(
            request = SampleRequest(productId, productName),
            endPoint = Constants.Endpoint.SAMPLE,
            responseClass = SampleResponse::class.java,
            header = RequestHeader(
                headerMap = mapOf(
                    Constants.HeaderKey.KEY to MlinkManager.getKey().orEmpty(),
                    Constants.HeaderKey.SDK_VERSION to BuildConfig.VERSION_NAME,
                    Constants.HeaderKey.TIMESTAMP to System.currentTimeMillis().toString(),
                )
            )
        )
    }
}