package com.mimeda.mlink

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.mimeda.mlink.common.MlinkConstants
import com.mimeda.mlink.common.MlinkConstants.ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.AID
import com.mimeda.mlink.common.MlinkConstants.ANDROID
import com.mimeda.mlink.common.MlinkConstants.APP_ID
import com.mimeda.mlink.common.MlinkConstants.CART
import com.mimeda.mlink.common.MlinkConstants.CART_VIEW
import com.mimeda.mlink.common.MlinkConstants.DATE_FORMAT
import com.mimeda.mlink.common.MlinkConstants.DEVICE_ID
import com.mimeda.mlink.common.MlinkConstants.EVENT
import com.mimeda.mlink.common.MlinkConstants.EVENT_PAGE
import com.mimeda.mlink.common.MlinkConstants.HOME
import com.mimeda.mlink.common.MlinkConstants.HOME_ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.HOME_VIEW
import com.mimeda.mlink.common.MlinkConstants.LANGUAGE
import com.mimeda.mlink.common.MlinkConstants.LISTING
import com.mimeda.mlink.common.MlinkConstants.LISTING_ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.LISTING_VIEW
import com.mimeda.mlink.common.MlinkConstants.MLINK_SESSION_ID
import com.mimeda.mlink.common.MlinkConstants.MLINK_TIME
import com.mimeda.mlink.common.MlinkConstants.MLINK_UUID
import com.mimeda.mlink.common.MlinkConstants.PLATFORM
import com.mimeda.mlink.common.MlinkConstants.PRODUCTS
import com.mimeda.mlink.common.MlinkConstants.PRODUCT_DETAILS
import com.mimeda.mlink.common.MlinkConstants.PRODUCT_DETAILS_ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.PRODUCT_DETAILS_VIEW
import com.mimeda.mlink.common.MlinkConstants.PUBLISHER
import com.mimeda.mlink.common.MlinkConstants.PURCHASE
import com.mimeda.mlink.common.MlinkConstants.PURCHASE_SUCCESS
import com.mimeda.mlink.common.MlinkConstants.SEARCH
import com.mimeda.mlink.common.MlinkConstants.SEARCH_ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.SEARCH_VIEW
import com.mimeda.mlink.common.MlinkConstants.SESSION_ID
import com.mimeda.mlink.common.MlinkConstants.SHARED_PREF_NAME
import com.mimeda.mlink.common.MlinkConstants.SUCCESS
import com.mimeda.mlink.common.MlinkConstants.THIRTY_MINUTES
import com.mimeda.mlink.common.MlinkConstants.TIMESTAMP
import com.mimeda.mlink.common.MlinkConstants.USER_ID
import com.mimeda.mlink.common.MlinkConstants.VERSION
import com.mimeda.mlink.common.MlinkConstants.VIEW
import com.mimeda.mlink.data.MlinkEventPayload
import com.mimeda.mlink.network.client.MlinkFuelClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object MlinkEvents {

    private val client by lazy { MlinkFuelClient() }
    private lateinit var sharedPref: SharedPreferences

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun prepareUrl(payload: MlinkEventPayload, event: String, eventPage: String): String {
        val uuid = if (sharedPref.getString(MLINK_UUID, "").isNullOrEmpty()) {
            UUID.randomUUID().toString().apply { sharedPref.edit().putString(MLINK_UUID, this).apply() }
        } else {
            sharedPref.getString(MLINK_UUID, "").orEmpty()
        }

        val productsString = payload.products?.joinToString(";") { "${it.barcode}:${it.quantity}:${it.price}" }
        val sessionId = getSessionId(payload.userId ?: -1, uuid)
        val language = "${Locale.getDefault().language}-${Locale.getDefault().country}"
        val platform = "${Build.MANUFACTURER.uppercase()}-${Build.MODEL}-$ANDROID-${Build.VERSION.RELEASE}"

        return buildString {
            append(BuildConfig.BASE_URL)
            appendParams(
                VERSION to BuildConfig.VERSION_NAME,
                PUBLISHER to MlinkConstants.publisher,
                APP_ID to MlinkConstants.appId,
                TIMESTAMP to System.currentTimeMillis().toString(),
                DEVICE_ID to uuid,
                LANGUAGE to language,
                PLATFORM to platform,
                EVENT to event,
                EVENT_PAGE to eventPage,
                AID to sharedPref.getString(MLINK_UUID, ""),
                USER_ID to payload.userId.toString(),
                SESSION_ID to sessionId,
                PRODUCTS to productsString
            )
        }
    }

    private fun getSessionId(userId: Int, uuid: String): String {
        val startTime = sharedPref.getLong(MLINK_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        val isThirtyMinutesPassed = currentTime - startTime >= THIRTY_MINUTES

        return when {
            startTime == 0L -> generateSessionId(userId, uuid)
            isThirtyMinutesPassed -> {
                val newUuid = UUID.randomUUID().toString().also {
                    sharedPref.edit().putString(MLINK_UUID, it).apply()
                }
                generateSessionId(userId, newUuid)
            }

            else -> sharedPref.getString(MLINK_SESSION_ID, "").orEmpty()
        }
    }

    private fun StringBuilder.appendParams(vararg params: Pair<String, Any?>) {
        params.forEach { (key, value) ->
            value?.let { append("$key=$it") }
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

    object Home {
        suspend fun view(payload: MlinkEventPayload) {
            client.get(prepareUrl(payload, HOME, VIEW), HOME_VIEW)
        }

        suspend fun addToCart(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) {
                MlinkLogger.warning("Mlink: You Should Send Products")
            }
            client.get(prepareUrl(payload, HOME, ADD_TO_CART), HOME_ADD_TO_CART)
        }
    }

    object Listing {
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.categoryId.isNullOrEmpty()) {
                MlinkLogger.warning("Mlink: You Should Send Category ID")
            }
            client.get(prepareUrl(payload, LISTING, VIEW), LISTING_VIEW)
        }

        suspend fun addToCart(payload: MlinkEventPayload) {
            when {
                payload.products.isNullOrEmpty() -> MlinkLogger.warning("Mlink: You Should Send Products")
                payload.categoryId.isNullOrEmpty() -> MlinkLogger.warning("Mlink: You Should Send Category ID")
            }
            client.get(prepareUrl(payload, LISTING, ADD_TO_CART), LISTING_ADD_TO_CART)
        }
    }

    object Search {
        suspend fun view(payload: MlinkEventPayload) {
            when {
                payload.keyword.isNullOrEmpty() -> MlinkLogger.warning("Mlink: You Should Send Keyword")
                payload.totalRowCount == null -> MlinkLogger.warning("Mlink: You Should Send Total Row Count")
            }
            client.get(prepareUrl(payload, SEARCH, VIEW), SEARCH_VIEW)
        }

        suspend fun addToCart(payload: MlinkEventPayload) {
            when {
                payload.products.isNullOrEmpty() -> MlinkLogger.warning("Mlink: You Should Send Products")
                payload.keyword.isNullOrEmpty() -> MlinkLogger.warning("Mlink: You Should Send Keyword")
            }
            client.get(prepareUrl(payload, SEARCH, ADD_TO_CART), SEARCH_ADD_TO_CART)
        }
    }

    object ProductDetails {
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) {
                MlinkLogger.warning("Mlink: You Should Send Products")
            }
            client.get(prepareUrl(payload, PRODUCT_DETAILS, VIEW), PRODUCT_DETAILS_VIEW)
        }

        suspend fun addToCart(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) {
                MlinkLogger.warning("Mlink: You Should Send Products")
            }
            client.get(prepareUrl(payload, PRODUCT_DETAILS, ADD_TO_CART), PRODUCT_DETAILS_ADD_TO_CART)
        }
    }

    object Cart {
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) {
                MlinkLogger.warning("Mlink: You Should Send Products")
            }
            client.get(prepareUrl(payload, CART, VIEW), CART_VIEW)
        }
    }

    object Purchase {
        suspend fun success(payload: MlinkEventPayload) {
            when {
                payload.transactionId == null -> MlinkLogger.warning("Mlink: You Should Send Transaction ID")
                payload.products.isNullOrEmpty() -> MlinkLogger.warning("Mlink: You Should Send Products")
            }
            client.get(prepareUrl(payload, PURCHASE, SUCCESS), PURCHASE_SUCCESS)
        }
    }
}