package com.mimeda.mlinkmobile.network

import com.mimeda.mlinkmobile.BuildConfig
import com.mimeda.mlinkmobile.MlinkManager
import com.mimeda.mlinkmobile.common.MlinkConstants
import com.mimeda.mlinkmobile.network.client.MlinkFuelClient
import com.mimeda.mlinkmobile.network.model.MlinkRequestHeader
import com.mimeda.mlinkmobile.network.model.MlinkSampleRequest
import com.mimeda.mlinkmobile.network.model.MlinkSampleResponse

class MlinkEventImpl : MlinkEvent {

    private val mlinkFuelClient by lazy { MlinkFuelClient() }

    override suspend fun sendSampleEvent(productId: Int) {
        mlinkFuelClient.post(
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
    }
}