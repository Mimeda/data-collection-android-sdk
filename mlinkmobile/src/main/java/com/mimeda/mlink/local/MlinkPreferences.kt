package com.mimeda.mlink.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import androidx.core.content.edit

internal object MlinkPreferences {

    private lateinit var sharedPref: SharedPreferences

    /**
     * Initializes EncryptedSharedPreferences with AES-256 encryption
     */
    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        sharedPref = EncryptedSharedPreferences.create(
            context,
            SHARED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    /**
     * Retrieves or generates a unique identifier (UUID) for the device
     */
    fun getUuid(): String {
        return if (sharedPref.getString(MLINK_UUID, "").isNullOrEmpty()) {
            UUID.randomUUID().toString().also { uuid ->
                sharedPref.edit { putString(MLINK_UUID, uuid) }
            }
        } else {
            sharedPref.getString(MLINK_UUID, "").orEmpty()
        }
    }

    /**
     * Retrieves or generates a session ID, resetting if more than 30 minutes have passed
     */
    fun getSessionId(userId: Int?): String {
        val startTime = sharedPref.getLong(MLINK_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        val isThirtyMinutesPassed = currentTime - startTime >= THIRTY_MINUTES

        return when {
            startTime == 0L -> generateSessionId(userId ?: -1, getUuid())
            isThirtyMinutesPassed -> {
                val newUuid = UUID.randomUUID().toString().also {
                    sharedPref.edit { putString(MLINK_UUID, it) }
                }
                generateSessionId(userId ?: -1, newUuid)
            }

            else -> sharedPref.getString(MLINK_SESSION_ID, "").orEmpty()
        }
    }

    /**
     * Generates a session ID based on user ID, UUID, and current timestamp
     */
    private fun generateSessionId(userId: Int, uuid: String): String {
        val time = System.currentTimeMillis()
        val formattedTime = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(time))
        sharedPref.edit {
            putLong(MLINK_TIME, time)
            putString(MLINK_SESSION_ID, "${userId}-$uuid/$formattedTime")
        }
        return "${userId}-$uuid/$formattedTime"
    }

    private const val SHARED_PREF_NAME = "MLinkSharedPref"
    private const val MLINK_UUID = "mLinkUUID"
    private const val MLINK_TIME = "mLinkTime"
    private const val MLINK_SESSION_ID = "mLinkSessionId"

    private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"
    private const val THIRTY_MINUTES = 30 * 60 * 1000
}