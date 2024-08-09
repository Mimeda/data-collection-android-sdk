package com.mimeda.mlinkmobile.network

import com.google.gson.annotations.SerializedName

internal data class ApiPostBody(
    @SerializedName("data")
    val data: String
)