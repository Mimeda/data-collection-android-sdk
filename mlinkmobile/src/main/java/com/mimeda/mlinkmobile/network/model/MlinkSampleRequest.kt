package com.mimeda.mlinkmobile.network.model

import com.google.gson.annotations.SerializedName

internal data class MlinkSampleRequest(
    @SerializedName("productId")
    val productId: Int,
) : MlinkBaseRequest()