package com.mimeda.mlinkmobile.data.repository

import android.os.Build
import com.mimeda.mlinkmobile.BuildConfig
import com.mimeda.mlinkmobile.common.MLinkConstants
import com.mimeda.mlinkmobile.data.model.Payload
import com.mimeda.mlinkmobile.network.client.MLinkFuelClient
import java.util.Locale

object MLinkEvents {

    private val client by lazy { MLinkFuelClient() }

    private fun getCoreUrl() = buildString {
        append(BuildConfig.BASE_URL)
        append("?v=${BuildConfig.VERSION_NAME}")
        append("&pub=${MLinkConstants.publisher}")
        append("&appid=${MLinkConstants.appId}")
        append("&t=${System.currentTimeMillis()}")
        append("&d=${Build.ID}")
        append("&lng=${Locale.getDefault().language}-${Locale.getDefault().country}")
        append("&p=Android-${Build.VERSION.SDK_INT}")
        //append("&s=${Mlink.Core.User.SessionID()}")
    }

    private fun prepareUrl(payload: Payload, event: String, eventPage: String) = buildString {
        append(getCoreUrl().plus("&en=$event&ep=$eventPage"))
        append("&aid=${payload.userId}")
        append("&uid=${payload.userId}")
        append("&li=${payload.lineItems.joinToString(",")}")
        val productsString = payload.products?.joinToString(";") { "${it.barcode}:${it.quantity}:${it.price}" }
        append("&pl=$productsString")
    }

    suspend fun homeView(payload: Payload) {
        client.get(prepareUrl(payload, "Home", "View"), "Home.View")
    }

    suspend fun homeAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, "Home", "AddToCart"), "Home.AddToCart")
    }

    suspend fun listingView(payload: Payload) {
        client.get(prepareUrl(payload, "Listing", "View"), "Listing.View")
    }

    suspend fun listingAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, "Listing", "AddToCart"), "Listing.AddToCart")
    }

    suspend fun searchView(payload: Payload) {
        client.get(prepareUrl(payload, "Search", "View"), "Search.View")
    }

    suspend fun searchAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, "Search", "AddToCart"), "Search.AddToCart")
    }

    suspend fun productDetailsView(payload: Payload) {
        client.get(prepareUrl(payload, "ProductDetails", "View"), "ProductDetails.View")
    }

    suspend fun productDetailsAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, "ProductDetails", "AddToCart"), "ProductDetails.AddToCart")
    }

    suspend fun cartView(payload: Payload) {
        client.get(prepareUrl(payload, "Cart", "View"), "Cart.View")
    }

    suspend fun purchaseSuccess(payload: Payload) {
        client.get(prepareUrl(payload, "Purchase", "Success"), "Purchase.Success")
    }
}