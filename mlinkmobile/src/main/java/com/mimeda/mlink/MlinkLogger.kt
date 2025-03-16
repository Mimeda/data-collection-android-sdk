package com.mimeda.mlink

import android.util.Log

internal object MlinkLogger {
    /**
     * Flag to enable or disable logging
     */
    var isEnabled = false

    private const val TAG = "MlinkLog"

    /**
     * Logs error messages if logging is enabled
     */
    fun error(message: String?) {
        if (isEnabled && message != null) Log.e(TAG, message)
    }

    /**
     * Logs informational messages if logging is enabled
     */
    fun info(message: String?) {
        if (isEnabled && message != null) Log.i(TAG, message)
    }

    /**
     * Logs debug messages if logging is enabled
     */
    fun debug(message: String?) {
        if (isEnabled && message != null) Log.d(TAG, message)
    }

    /**
     * Logs verbose messages if logging is enabled
     */
    fun warning(message: String?) {
        if (isEnabled && message != null) Log.w(TAG, message)
    }
}