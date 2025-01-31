package com.mimeda.mlink.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

internal object MlinkPreferences {

    private lateinit var sharedPref: SharedPreferences

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        sharedPref = EncryptedSharedPreferences.create(
            context,
            SHARED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

        val prefsFile = File(context.filesDir.parent, "/shared_prefs/$SHARED_PREF_NAME.xml")

        if (prefsFile.exists()) {
            Log.d("MlinkCheck", "SharedPreferences dosyası mevcut: ${prefsFile.absolutePath}")

            try {
                val fileContent = prefsFile.readText()
                Log.d("MlinkCheck", "Şifrelenmiş İçerik:\n$fileContent")
            } catch (e: Exception) {
                Log.e("MlinkCheck", "Dosya okunamadı: ${e.message}")
            }
        } else {
            Log.e("MlinkCheck", "SharedPreferences dosyası bulunamadı!")
        }
    }

    fun getUuid(): String {
        return if (sharedPref.getString(MLINK_UUID, "").isNullOrEmpty()) {
            UUID.randomUUID().toString().apply { sharedPref.edit().putString(MLINK_UUID, this).apply() }
        } else {
            sharedPref.getString(MLINK_UUID, "").orEmpty()
        }
    }

    fun getSessionId(userId: Int?): String {
        val startTime = sharedPref.getLong(MLINK_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        val isThirtyMinutesPassed = currentTime - startTime >= THIRTY_MINUTES

        return when {
            startTime == 0L -> generateSessionId(userId ?: -1, getUuid())
            isThirtyMinutesPassed -> {
                val newUuid = UUID.randomUUID().toString().also {
                    sharedPref.edit().putString(MLINK_UUID, it).apply()
                }
                generateSessionId(userId ?: -1, newUuid)
            }

            else -> sharedPref.getString(MLINK_SESSION_ID, "").orEmpty()
        }
    }

    private fun generateSessionId(userId: Int, uuid: String): String {
        val time = System.currentTimeMillis()
        val formattedTime = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(time))
        sharedPref.edit().apply {
            putLong(MLINK_TIME, time)
            putString(MLINK_SESSION_ID, "${userId}-$uuid/$formattedTime")
        }.apply()
        return "${userId}-$uuid/$formattedTime"
    }

    private const val SHARED_PREF_NAME = "MLinkSharedPref"
    private const val MLINK_UUID = "mLinkUUID"
    private const val MLINK_TIME = "mLinkTime"
    private const val MLINK_SESSION_ID = "mLinkSessionId"

    private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"
    private const val THIRTY_MINUTES = 30 * 60 * 1000
}