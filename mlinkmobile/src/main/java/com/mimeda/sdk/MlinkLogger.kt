package com.mimeda.sdk

import android.util.Log

internal object MlinkLogger {

    var isEnabled = false

    private const val TAG = "MlinkLog"

    fun error(message: String?) {
        if (isEnabled && message != null) Log.e(TAG, message)
    }

    fun info(message: String?) {
        if (isEnabled && message != null) Log.i(TAG, message)
    }

    fun debug(message: String?) {
        if (isEnabled && message != null) Log.d(TAG, message)
    }

    fun warning(message: String?) {
        if (isEnabled && message != null) Log.w(TAG, message)
    }
}