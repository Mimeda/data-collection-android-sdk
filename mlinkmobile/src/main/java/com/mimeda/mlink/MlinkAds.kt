package com.mimeda.mlink

import android.content.Context
import android.content.SharedPreferences
import com.mimeda.mlink.common.MlinkConstants.AD_UNIT
import com.mimeda.mlink.common.MlinkConstants.CLICK
import com.mimeda.mlink.common.MlinkConstants.CLICK_URL
import com.mimeda.mlink.common.MlinkConstants.CREATIVE_ID
import com.mimeda.mlink.common.MlinkConstants.IMPRESSION
import com.mimeda.mlink.common.MlinkConstants.IMPRESSION_URL
import com.mimeda.mlink.common.MlinkConstants.KEYWORD_AD
import com.mimeda.mlink.common.MlinkConstants.LINE_ITEM_ID
import com.mimeda.mlink.common.MlinkConstants.SHARED_PREF_NAME
import com.mimeda.mlink.data.MlinkAdPayload
import com.mimeda.mlink.data.UrlPath
import com.mimeda.mlink.network.client.MlinkFuelClient

object MlinkAds {

    private val client by lazy { MlinkFuelClient() }
    private lateinit var sharedPref: SharedPreferences

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun prepareUrl(payload: MlinkAdPayload, urlPath: UrlPath): String {
        return buildString {
            append(BuildConfig.BASE_URL)
            append(urlPath.value)
            appendParams(
                LINE_ITEM_ID to payload.lineItemId,
                CREATIVE_ID to payload.creativeId,
                AD_UNIT to payload.adUnit,
                KEYWORD_AD to payload.keyword,
            )
        }
    }

    private fun StringBuilder.appendParams(vararg params: Pair<String, Any?>) {
        params.forEach { (key, value) ->
            value?.let { append("$key=$it") }
        }
    }

    suspend fun impression(payload: MlinkAdPayload) {
        client.get(prepareUrl(payload, UrlPath.IMPRESSION), IMPRESSION)
    }

    suspend fun click(payload: MlinkAdPayload) {
        client.get(prepareUrl(payload, UrlPath.CLICK), CLICK)
    }
}