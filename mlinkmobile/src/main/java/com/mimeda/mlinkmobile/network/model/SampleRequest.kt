package com.mimeda.mlinkmobile.network.model

import com.google.gson.annotations.SerializedName

internal data class SampleRequest(
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("productName")
    val productName: String,
) : BaseRequest()