package com.mimeda.mlinkmobile.network

import com.mimeda.mlinkmobile.BuildConfig
import com.mimeda.mlinkmobile.MlinkLogger
import com.mimeda.mlinkmobile.MlinkManager
import com.mimeda.mlinkmobile.common.MlinkConstants
import com.mimeda.mlinkmobile.common.MlinkResource
import com.mimeda.mlinkmobile.network.client.MlinkFuelClient
import com.mimeda.mlinkmobile.network.model.MlinkSampleRequest
import com.mimeda.mlinkmobile.network.model.MlinkSampleResponse

class MlinkEventImpl : MlinkEvent {

    private val mlinkFuelClient by lazy { MlinkFuelClient() }

    override suspend fun sendSampleEvent(productId: Int) {
        when (
            val result = mlinkFuelClient.post(
                request = MlinkSampleRequest(productId),
                endPoint = MlinkConstants.Endpoint.SAMPLE,
                responseClass = MlinkSampleResponse::class.java,
                header = MlinkRequestHeader(
                    headerMap = mapOf(
                        MlinkConstants.HeaderKey.KEY to MlinkManager.getKey().orEmpty(),
                        MlinkConstants.HeaderKey.SDK_VERSION to BuildConfig.VERSION_NAME,
                        MlinkConstants.HeaderKey.TIMESTAMP to System.currentTimeMillis().toString(),
                    )
                )
            )
        ) {
            is MlinkResource.Success -> {
                MlinkLogger.info("Product ID: $productId sent successfully")
            }

            is MlinkResource.Error -> {
                MlinkLogger.error("Product ID: $productId could not be sent. Error: ${result.error.message}")
            }
        }
    }
}