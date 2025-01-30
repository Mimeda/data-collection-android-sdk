package com.mimeda.mlink

import android.content.Context
import android.content.SharedPreferences
import com.mimeda.mlink.common.MlinkConstants.AD_UNIT
import com.mimeda.mlink.common.MlinkConstants.AID
import com.mimeda.mlink.common.MlinkConstants.CLICK
import com.mimeda.mlink.common.MlinkConstants.CREATIVE_ID
import com.mimeda.mlink.common.MlinkConstants.IMPRESSION
import com.mimeda.mlink.common.MlinkConstants.KEYWORD_AD
import com.mimeda.mlink.common.MlinkConstants.LINE_ITEM_ID
import com.mimeda.mlink.common.MlinkConstants.MLINK_UUID
import com.mimeda.mlink.common.MlinkConstants.PAYLOAD
import com.mimeda.mlink.common.MlinkConstants.PRODUCT_SKU
import com.mimeda.mlink.common.MlinkConstants.SESSION_ID
import com.mimeda.mlink.common.MlinkConstants.SHARED_PREF_NAME
import com.mimeda.mlink.common.MlinkConstants.TIMESTAMP
import com.mimeda.mlink.common.MlinkConstants.USER_ID
import com.mimeda.mlink.common.appendParams
import com.mimeda.mlink.common.getSessionId
import com.mimeda.mlink.common.prepareUuid
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
        val uuid = sharedPref.prepareUuid()

        val sessionId = sharedPref.getSessionId(payload.userId ?: -1, uuid)
        return buildString {
            append(BuildConfig.PERFORMANCE_URL)
            append(urlPath.value)
            appendParams(
                CREATIVE_ID to payload.creativeId,
                LINE_ITEM_ID to payload.lineItemId,
                AD_UNIT to payload.adUnit,
                KEYWORD_AD to payload.keyword,
                PRODUCT_SKU to payload.productSku,
                PAYLOAD to payload.payload,
                AID to sharedPref.getString(MLINK_UUID, ""),
                USER_ID to payload.userId.toString(),
                TIMESTAMP to System.currentTimeMillis(),
                SESSION_ID to sessionId,
            )
        }
    }

    suspend fun impression(payload: MlinkAdPayload) {
        client.get(prepareUrl(payload, UrlPath.IMPRESSION), IMPRESSION)
    }

    suspend fun click(payload: MlinkAdPayload) {
        client.get(prepareUrl(payload, UrlPath.CLICK), CLICK)
    }
}