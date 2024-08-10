package com.mimeda.mlinkmobile

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.Keep

@Keep
object MlinkManager {

    private var key: String? = ""

    fun init(
        context: Context,
        isLogEnabled: Boolean = true,
    ) {
        MlinkLogger.isEnabled = isLogEnabled
        context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).apply {
            key = metaData.getString("com.mimeda.mlinkmobile.key")
        }
        MlinkLogger.info("MlinkManager initialized successfully.")
    }

    fun getKey(): String? = key
}