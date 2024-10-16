package com.mimeda.sdk

import android.content.Context
import androidx.annotation.Keep
import com.mimeda.sdk.common.MLinkConstants
import com.mimeda.sdk.data.repository.MLinkEvents

@Keep
object MLink {
    fun init(context: Context, appId: Int, publisher: String, isLogEnabled: Boolean = true) {
        MLinkLogger.isEnabled = isLogEnabled
        MLinkConstants.appId = appId
        MLinkConstants.publisher = publisher
        MLinkEvents.init(context)
        MLinkLogger.info("MLinkManager initialized successfully.")
    }
}