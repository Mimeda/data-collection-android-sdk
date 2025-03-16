package com.mimeda.mlink.common

import android.os.Build
import com.mimeda.mlink.BuildConfig
import com.mimeda.mlink.data.MlinkAdPayload
import com.mimeda.mlink.data.MlinkEventPayload
import com.mimeda.mlink.data.UrlPath
import com.mimeda.mlink.local.MlinkPreferences
import java.util.Locale

internal object MlinkUrlBuilder {

    fun prepareAdUrl(payload: MlinkAdPayload, urlPath: UrlPath): String {
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
                AID to MlinkPreferences.getUuid(),
                USER_ID to payload.userId.toString(),
                TIMESTAMP to System.currentTimeMillis(),
                SESSION_ID to MlinkPreferences.getSessionId(payload.userId),
            )
        }
    }

    fun prepareEventUrl(payload: MlinkEventPayload, event: String, eventPage: String): String {
        val productsString = payload.products?.joinToString(";") { "${it.barcode}:${it.quantity}:${it.price}" }
        val lineItemIds = payload.lineItemIds?.joinToString(",")
        val sessionId = MlinkPreferences.getSessionId(payload.userId)
        val language = "${Locale.getDefault().language}-${Locale.getDefault().country}"
        val platform = "${Build.MANUFACTURER.uppercase()}-${Build.MODEL}-$ANDROID-${Build.VERSION.RELEASE}"

        return buildString {
            append(BuildConfig.EVENT_URL)
            append(UrlPath.EVENT.value)
            appendParams(
                VERSION to BuildConfig.VERSION_NAME,
                PUBLISHER to MlinkConstants.publisher,
                APP_ID to MlinkConstants.appId,
                TIMESTAMP to System.currentTimeMillis(),
                DEVICE_ID to MlinkPreferences.getUuid(),
                LANGUAGE to language,
                PLATFORM to platform,
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
                WEBSITE to payload.website,
                LOYALTY_CARD to payload.loyaltyCard,
            )
        }
    }

    private fun StringBuilder.appendParams(vararg params: Pair<String, Any?>) {
        params.forEach { (key, value) ->
            value?.let { append("$key=$it") }
        }
    }

    private const val ANDROID = "Android"
    private const val VERSION = "v"
    private const val PUBLISHER = "&pub"
    private const val APP_ID = "&appId"
    private const val TIMESTAMP = "&t"
    private const val DEVICE_ID = "&d"
    private const val LANGUAGE = "&lng"
    private const val PLATFORM = "&p"
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
    private const val WEBSITE = "&ws"
    private const val LOYALTY_CARD = "&lc"
}