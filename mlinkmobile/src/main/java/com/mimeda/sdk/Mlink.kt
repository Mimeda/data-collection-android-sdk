package com.mimeda.sdk

import android.content.Context
import androidx.annotation.Keep
import com.mimeda.sdk.common.MlinkConstants

@Keep
object Mlink {
    fun initialize(context: Context, appId: Int, publisher: String, isLogEnabled: Boolean = false) {
        MlinkLogger.isEnabled = isLogEnabled
        MlinkConstants.appId = appId
        MlinkConstants.publisher = publisher
        MlinkEvents.init(context)
        MlinkLogger.info("Mlink initialized successfully.")
    }
}