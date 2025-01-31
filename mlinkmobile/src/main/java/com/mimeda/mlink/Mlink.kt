package com.mimeda.mlink

import android.content.Context
import androidx.annotation.Keep
import com.mimeda.mlink.common.MlinkConstants
import com.mimeda.mlink.local.MlinkPreferences

@Keep
object Mlink {
    fun initialize(context: Context, appId: String, publisher: String, isLogEnabled: Boolean = false) {
        MlinkLogger.isEnabled = isLogEnabled
        MlinkConstants.appId = appId
        MlinkConstants.publisher = publisher
        MlinkPreferences.init(context)
        MlinkLogger.info("Mlink initialized successfully.")
    }
}