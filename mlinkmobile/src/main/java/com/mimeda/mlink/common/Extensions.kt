package com.mimeda.mlink.common

import android.content.SharedPreferences
import com.mimeda.mlink.common.MlinkConstants.DATE_FORMAT
import com.mimeda.mlink.common.MlinkConstants.MLINK_SESSION_ID
import com.mimeda.mlink.common.MlinkConstants.MLINK_TIME
import com.mimeda.mlink.common.MlinkConstants.MLINK_UUID
import com.mimeda.mlink.common.MlinkConstants.THIRTY_MINUTES
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

fun SharedPreferences.prepareUuid(): String {
    return if (getString(MLINK_UUID, "").isNullOrEmpty()) {
        UUID.randomUUID().toString().apply { edit().putString(MLINK_UUID, this).apply() }
    } else {
        getString(MLINK_UUID, "").orEmpty()
    }
}

fun SharedPreferences.getSessionId(userId: Int, uuid: String): String {
    val startTime = getLong(MLINK_TIME, 0L)
    val currentTime = System.currentTimeMillis()
    val isThirtyMinutesPassed = currentTime - startTime >= THIRTY_MINUTES

    return when {
        startTime == 0L -> generateSessionId(userId, uuid)
        isThirtyMinutesPassed -> {
            val newUuid = UUID.randomUUID().toString().also {
                edit().putString(MLINK_UUID, it).apply()
            }
            generateSessionId(userId, newUuid)
        }

        else -> getString(MLINK_SESSION_ID, "").orEmpty()
    }
}

fun SharedPreferences.generateSessionId(userId: Int, uuid: String): String {
    val time = System.currentTimeMillis()
    val formattedTime = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(time))
    edit().apply {
        putLong(MLINK_TIME, time)
        putString(MLINK_SESSION_ID, "${userId}-$uuid/$formattedTime")
    }.apply()
    return "${userId}-$uuid/$formattedTime"
}

fun StringBuilder.appendParams(vararg params: Pair<String, Any?>) {
    params.forEach { (key, value) ->
        value?.let { append("$key=$it") }
    }
}