package com.mimeda.mlink

import android.content.Context
import androidx.annotation.Keep
import com.mimeda.mlink.common.MlinkConstants
import com.mimeda.mlink.local.MlinkPreferences

@Keep
object Mlink {
    /**
     * Initializes the Mlink library with required configurations
     */
    fun initialize(
        context: Context,
        app: String,
        isLogEnabled: Boolean = false,
    ) {
        // Enable or disable logging
        MlinkLogger.isEnabled = isLogEnabled

        // Set application and publisher details
        MlinkConstants.app = app

        // Initialize preferences storage
        MlinkPreferences.init(context)

        // Log initialization success message
        MlinkLogger.info("Mlink initialized successfully.")
    }
}
