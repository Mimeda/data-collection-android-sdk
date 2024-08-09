package com.mimeda.mlinkmobile.network

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class Meta(

    @Keep
    @SerializedName("requestId")
    val requestId: String? = null,

    @Keep
    @SerializedName("httpStatusCode")
    val httpStatusCode: Int? = null,

    @Keep
    @SerializedName("errorMessage")
    val errorMessage: String? = null,

    @Keep
    @SerializedName("errorCode")
    val errorCode: Int? = null
)