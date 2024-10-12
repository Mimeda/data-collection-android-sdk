package com.mimeda.mlinkmobile

import androidx.annotation.Keep
import com.mimeda.mlinkmobile.common.MLinkConstants

@Keep
object MLink {
    fun init(appId: Int, publisher: String, isLogEnabled: Boolean = true) {
        MLinkLogger.isEnabled = isLogEnabled
        MLinkConstants.appId = appId
        MLinkConstants.publisher = publisher
        MLinkLogger.info("MLinkManager initialized successfully.")
    }
}