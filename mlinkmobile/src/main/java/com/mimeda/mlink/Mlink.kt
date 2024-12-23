package com.mimeda.mlink

import android.content.Context
import androidx.annotation.Keep
import com.mimeda.mlink.common.MlinkConstants

@Keep
object Mlink {
    fun initialize(context: Context, appId: Int, publisher: String, isLogEnabled: Boolean = false) {
        MlinkLogger.isEnabled = isLogEnabled
        MlinkConstants.appId = appId
        MlinkConstants.publisher = publisher
        MlinkEvents.init(context)
        MlinkAds.init(context)
        MlinkLogger.info("Mlink initialized successfully.")
    }
}