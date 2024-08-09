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
        Logger.isEnabled = isLogEnabled
        context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).apply {
            key = metaData.getString("com.mimeda.mlinkmobile.key")
        }
    }

    fun getKey(): String? = key
}