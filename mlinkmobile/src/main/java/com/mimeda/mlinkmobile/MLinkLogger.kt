package com.mimeda.mlinkmobile

import android.util.Log

internal object MLinkLogger {

    var isEnabled = false

    private const val TAG = "MLinkLog"

    fun error(message: String?) {
        if (isEnabled && message != null) Log.e(TAG, message)
    }

    fun info(message: String?) {
        if (isEnabled && message != null) Log.i(TAG, message)
    }

    fun debug(message: String?) {
        if (isEnabled && message != null) Log.d(TAG, message)
    }
}