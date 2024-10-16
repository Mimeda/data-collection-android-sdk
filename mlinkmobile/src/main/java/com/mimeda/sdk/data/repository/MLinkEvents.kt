package com.mimeda.sdk.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.mimeda.sdk.BuildConfig
import com.mimeda.sdk.common.MLinkConstants
import com.mimeda.sdk.common.MLinkConstants.ADD_TO_CART
import com.mimeda.sdk.common.MLinkConstants.AID
import com.mimeda.sdk.common.MLinkConstants.APP_ID
import com.mimeda.sdk.common.MLinkConstants.CART
import com.mimeda.sdk.common.MLinkConstants.CART_VIEW
import com.mimeda.sdk.common.MLinkConstants.DATE_FORMAT
import com.mimeda.sdk.common.MLinkConstants.DEVICE_ID
import com.mimeda.sdk.common.MLinkConstants.EVENT
import com.mimeda.sdk.common.MLinkConstants.EVENT_PAGE
import com.mimeda.sdk.common.MLinkConstants.HOME
import com.mimeda.sdk.common.MLinkConstants.HOME_ADD_TO_CART
import com.mimeda.sdk.common.MLinkConstants.HOME_VIEW
import com.mimeda.sdk.common.MLinkConstants.LANGUAGE
import com.mimeda.sdk.common.MLinkConstants.LINE_ITEMS
import com.mimeda.sdk.common.MLinkConstants.LISTING
import com.mimeda.sdk.common.MLinkConstants.LISTING_ADD_TO_CART
import com.mimeda.sdk.common.MLinkConstants.LISTING_VIEW
import com.mimeda.sdk.common.MLinkConstants.MLINK_SESSION_ID
import com.mimeda.sdk.common.MLinkConstants.MLINK_TIME
import com.mimeda.sdk.common.MLinkConstants.MLINK_UUID
import com.mimeda.sdk.common.MLinkConstants.PLATFORM
import com.mimeda.sdk.common.MLinkConstants.PRODUCTS
import com.mimeda.sdk.common.MLinkConstants.PRODUCT_DETAILS
import com.mimeda.sdk.common.MLinkConstants.PRODUCT_DETAILS_ADD_TO_CART
import com.mimeda.sdk.common.MLinkConstants.PRODUCT_DETAILS_VIEW
import com.mimeda.sdk.common.MLinkConstants.PUBLISHER
import com.mimeda.sdk.common.MLinkConstants.PURCHASE
import com.mimeda.sdk.common.MLinkConstants.PURCHASE_SUCCESS
import com.mimeda.sdk.common.MLinkConstants.SEARCH
import com.mimeda.sdk.common.MLinkConstants.SEARCH_ADD_TO_CART
import com.mimeda.sdk.common.MLinkConstants.SEARCH_VIEW
import com.mimeda.sdk.common.MLinkConstants.SESSION_ID
import com.mimeda.sdk.common.MLinkConstants.SHARED_PREF_NAME
import com.mimeda.sdk.common.MLinkConstants.SUCCESS
import com.mimeda.sdk.common.MLinkConstants.THIRTY_MINUTES
import com.mimeda.sdk.common.MLinkConstants.TIMESTAMP
import com.mimeda.sdk.common.MLinkConstants.USER_ID
import com.mimeda.sdk.common.MLinkConstants.VERSION
import com.mimeda.sdk.common.MLinkConstants.VIEW
import com.mimeda.sdk.data.model.Payload
import com.mimeda.sdk.network.client.MLinkFuelClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object MLinkEvents {

    private val client by lazy { MLinkFuelClient() }
    private lateinit var sharedPref: SharedPreferences

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun prepareUrl(payload: Payload, event: String, eventPage: String) = buildString {
        val uuid = if (sharedPref.getString(MLINK_UUID, "").isNullOrEmpty()) {
            UUID.randomUUID().toString().apply {
                sharedPref.edit().putString(MLINK_UUID, this).apply()
            }
        } else {
            sharedPref.getString(MLINK_UUID, "").orEmpty()
        }

        val productsString = payload.products?.joinToString(";") { "${it.barcode}:${it.quantity}:${it.price}" }

        val startTime = sharedPref.getLong(MLINK_TIME, 0L)
        val currentTime = System.currentTimeMillis()
        val differenceInMillis = currentTime - startTime
        val isThirtyMinutesPassed = differenceInMillis >= THIRTY_MINUTES

        val sessionId = when {
            startTime == 0L -> generateSessionId(payload.userId, uuid)
            isThirtyMinutesPassed -> {
                val newUuid = UUID.randomUUID().toString().apply {
                    sharedPref.edit().putString(MLINK_UUID, this).apply()
                }
                generateSessionId(payload.userId, newUuid)
            }
            else -> sharedPref.getString(MLINK_SESSION_ID, "").orEmpty()
        }

        append(BuildConfig.BASE_URL)
        append("$VERSION=${BuildConfig.VERSION_NAME}")
        append("$PUBLISHER=${MLinkConstants.publisher}")
        append("$APP_ID=${MLinkConstants.appId}")
        append("$TIMESTAMP=${System.currentTimeMillis()}")
        append("$DEVICE_ID=$uuid")
        append("$LANGUAGE=${Locale.getDefault().language}-${Locale.getDefault().country}")
        append("$PLATFORM=${Build.MANUFACTURER.uppercase()}-${Build.MODEL}-Android-${Build.VERSION.RELEASE}")
        append("$EVENT=$event")
        append("$EVENT_PAGE=$eventPage")
        append("$AID=${sharedPref.getString(MLINK_UUID, "")}")
        append("$USER_ID=${payload.userId}")
        append("$SESSION_ID=$sessionId")
        append("$LINE_ITEMS=${payload.lineItems.joinToString(",")}")
        append("$PRODUCTS=$productsString")
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

    suspend fun homeView(payload: Payload) {
        client.get(prepareUrl(payload, HOME, VIEW), HOME_VIEW)
    }

    suspend fun homeAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, HOME, ADD_TO_CART), HOME_ADD_TO_CART)
    }

    suspend fun listingView(payload: Payload) {
        val url = prepareUrl(payload, LISTING, VIEW)
        println(url)
        client.get(url, LISTING_VIEW)
    }

    suspend fun listingAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, LISTING, ADD_TO_CART), LISTING_ADD_TO_CART)
    }

    suspend fun searchView(payload: Payload) {
        client.get(prepareUrl(payload, SEARCH, VIEW), SEARCH_VIEW)
    }

    suspend fun searchAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, SEARCH, ADD_TO_CART), SEARCH_ADD_TO_CART)
    }

    suspend fun productDetailsView(payload: Payload) {
        client.get(prepareUrl(payload, PRODUCT_DETAILS, VIEW), PRODUCT_DETAILS_VIEW)
    }

    suspend fun productDetailsAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, PRODUCT_DETAILS, ADD_TO_CART), PRODUCT_DETAILS_ADD_TO_CART)
    }

    suspend fun cartView(payload: Payload) {
        client.get(prepareUrl(payload, CART, VIEW), CART_VIEW)
    }

    suspend fun purchaseSuccess(payload: Payload) {
        client.get(prepareUrl(payload, PURCHASE, SUCCESS), PURCHASE_SUCCESS)
    }
}