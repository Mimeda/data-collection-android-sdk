package com.mimeda.mlinkmobile.network

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal open class BaseResponse<T : BaseResult> {

    @Keep
    @SerializedName("result")
    lateinit var result: T

    @Keep
    @SerializedName("meta")
    val meta: Meta? = null
}