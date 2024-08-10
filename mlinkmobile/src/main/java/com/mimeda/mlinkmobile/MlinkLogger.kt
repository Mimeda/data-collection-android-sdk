package com.mimeda.mlinkmobile

import android.util.Log
import com.mimeda.mlinkmobile.network.MlinkRequestHeader

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

    fun warn(message: String?) {
        if (isEnabled && message != null) Log.w(TAG, message)
    }

    fun verbose(message: String?) {
        if (isEnabled && message != null) Log.v(TAG, message)
    }

    fun printNetworkRequest(url: String, request: String, header: MlinkRequestHeader) {
        if (BuildConfig.DEBUG) {
            verbose("URL:$url\nHEADER:$header\nBODY:$request")
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