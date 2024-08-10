package com.mimeda.mlinkmobile.network.model

import com.google.gson.annotations.SerializedName

internal data class MlinkApiPostBody(
    @SerializedName("data")
    val data: String
)