package com.mimeda.mlink.common

import com.mimeda.mlink.BuildConfig
import com.mimeda.mlink.data.MlinkAdPayload
import com.mimeda.mlink.data.MlinkEventPayload
import com.mimeda.mlink.data.UrlPath
import com.mimeda.mlink.local.MlinkPreferences
import java.util.Locale

internal object MlinkUrlBuilder {
    /**
     * Prepares an advertisement URL using the provided ad payload and URL path
     */
    fun prepareAdUrl(payload: MlinkAdPayload, urlPath: UrlPath): String {
        return buildString {
            append(BuildConfig.PERFORMANCE_URL)
            append(urlPath.value)
            appendParams(
                CREATIVE_ID to payload.creativeId,
                LINE_ITEM_ID to payload.lineItemId,
                AD_UNIT to payload.adUnit,
                APP to MlinkConstants.app,
                KEYWORD_AD to payload.keyword,
                PRODUCT_SKU to payload.productSku,
                OS to ANDROID,
                PAYLOAD to payload.payload,
                AID to MlinkPreferences.getUuid(),
                USER_ID to payload.userId.toString(),
                TIMESTAMP to System.currentTimeMillis(),
                SESSION_ID to MlinkPreferences.getSessionId(payload.userId),
            )
        }
    }

    /**
     * Prepares an event tracking URL using the provided event payload
     */
    fun prepareEventUrl(payload: MlinkEventPayload, event: String, eventPage: String): String {
        val productsString = payload.products?.joinToString(";") { "${it.barcode}:${it.quantity}:${it.price}" }
        val lineItemIds = payload.lineItemIds?.joinToString(",")
        val sessionId = MlinkPreferences.getSessionId(payload.userId)
        val language = "${Locale.getDefault().language}-${Locale.getDefault().country}"

        return buildString {
            append(BuildConfig.EVENT_URL)
            append(UrlPath.EVENT.value)
            appendParams(
                VERSION to BuildConfig.VERSION_NAME,
                APP to MlinkConstants.app,
                TIMESTAMP to System.currentTimeMillis(),
                DEVICE_ID to MlinkPreferences.getUuid(),
                LANGUAGE to language,
                OS to ANDROID,
                EVENT to event,
                EVENT_PAGE to eventPage,
                AID to MlinkPreferences.getUuid(),
                USER_ID to payload.userId.toString(),
                SESSION_ID to sessionId,
                PRODUCTS to productsString,
                CATEGORY_ID to payload.categoryId,
                KEYWORD to payload.keyword,
                TRANSACTION_ID to payload.transactionId,
                TOTAL_ROW_COUNT to payload.totalRowCount,
                LINE_ITEM_ID to lineItemIds,
                LOYALTY_CARD to payload.loyaltyCard,
            )
        }
    }

    /**
     * Appends key-value parameters to the URL query string
     */
    private fun StringBuilder.appendParams(vararg params: Pair<String, Any?>) {
        params.forEach { (key, value) ->
            value?.let { append("$key=$it") }
        }
    }

    private const val ANDROID = "Android"
    private const val VERSION = "v"
    private const val APP = "&app"
    private const val TIMESTAMP = "&t"
    private const val DEVICE_ID = "&d"
    private const val LANGUAGE = "&lng"
    private const val OS = "&os"
    private const val EVENT = "&en"
    private const val EVENT_PAGE = "&ep"
    private const val AID = "&aid"
    private const val USER_ID = "&uid"
    private const val SESSION_ID = "&s"
    private const val PRODUCTS = "&pl"
    private const val CATEGORY_ID = "&ct"
    private const val KEYWORD = "&kw"
    private const val TRANSACTION_ID = "&trans"
    private const val TOTAL_ROW_COUNT = "&trc"

    private const val CREATIVE_ID = "c"
    private const val LINE_ITEM_ID = "&li"
    private const val AD_UNIT = "&au"
    private const val KEYWORD_AD = "&kw"
    private const val PRODUCT_SKU = "&psku"
    private const val PAYLOAD = "&pyl"
    private const val LOYALTY_CARD = "&lc"
}