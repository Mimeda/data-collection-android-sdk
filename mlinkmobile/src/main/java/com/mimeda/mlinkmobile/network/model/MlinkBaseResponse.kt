package com.mimeda.mlinkmobile.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class MlinkBaseResponse<T : MlinkBaseResult> {

    @Keep
    @SerializedName("result")
    lateinit var result: T

    @Keep
    @SerializedName("mlinkMeta")
    val mlinkMeta: MlinkMeta? = null
}