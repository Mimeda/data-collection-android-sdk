package com.mimeda.mlinkmobile

import android.util.Log
import com.mimeda.mlinkmobile.network.RequestHeader

internal object Logger {

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

    fun warn(message: String?) {
        if (isEnabled && message != null) Log.w(TAG, message)
    }

    fun verbose(message: String?) {
        if (isEnabled && message != null) Log.v(TAG, message)
    }

    fun printNetworkRequest(url: String, request: String, header: RequestHeader) {
        if (BuildConfig.DEBUG) {
            verbose("URL:\n$url\nHEADER:\n$header\nBODY:\n$request")
        }
    }

    fun printNetworkResponse(isSuccessful: Boolean, state: String, response: String) {
        if (BuildConfig.DEBUG) {
            if (isSuccessful) {
                debug("$state:\n$response")
            } else {
                error("$state:\n$response")
            }
        } else {
            if (isSuccessful) {
                debug(state)
            } else {
                error(state)
            }
        }
    }
}