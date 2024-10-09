package com.mimeda.mlinkmobile.data.repository

import android.os.Build
import com.mimeda.mlinkmobile.BuildConfig
import com.mimeda.mlinkmobile.data.model.Payload
import com.mimeda.mlinkmobile.network.client.MLinkFuelClient
import java.util.Locale

internal class MLinkRepositoryImpl(private val client: MLinkFuelClient) : MLinkRepository {

    private val coreUrl = buildString {
        append(BuildConfig.BASE_URL)
        append("?v=${BuildConfig.VERSION_NAME}")
        append("&pub=publisher")
        append("&t=${System.currentTimeMillis()}")
        append("&d=${Build.DEVICE}")
        //append("&aid=${Mlink.Core.User.AID()}")
        //append("&uid=${Mlink.Core.User.ID()}")
        append("&lng=${Locale.getDefault().language}")
        append("&p=${Build.PRODUCT}-${Build.VERSION.SDK_INT}")
        //append("&s=${Mlink.Core.User.SessionID()}")
    }

    private fun prepareUrl(payload: Payload, event: String, eventPage: String): String {
        val url = coreUrl.plus("&en=$event&ep=$eventPage")
        url.plus("&li=${payload.lineItems.joinToString(",")}")
        val productsString = payload.products.joinToString(";") {
            "${it.barcode}:${it.quantity}:${it.price}"
        }
        url.plus("&pl=$productsString")
        return url
    }

    override suspend fun homeView(payload: Payload) {
        client.get(prepareUrl(payload, "Home", "View"), "Home.View")
    }

    override suspend fun homeAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, "Home", "AddToCart"), "Home.AddToCart")
    }

    override suspend fun listingView(payload: Payload) {
        client.get(prepareUrl(payload, "Listing", "View"), "Listing.View")
    }

    override suspend fun listingAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, "Listing", "AddToCart"), "Listing.AddToCart")
    }

    override suspend fun searchView(payload: Payload) {
        client.get(prepareUrl(payload, "Search", "View"), "Search.View")
    }

    override suspend fun searchAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, "Search", "AddToCart"), "Search.AddToCart")
    }

    override suspend fun productDetailsView(payload: Payload) {
        client.get(prepareUrl(payload, "ProductDetails", "View"), "ProductDetails.View")
    }

    override suspend fun productDetailsAddToCart(payload: Payload) {
        client.get(prepareUrl(payload, "ProductDetails", "AddToCart"), "ProductDetails.AddToCart")
    }

    override suspend fun cartView(payload: Payload) {
        client.get(prepareUrl(payload, "Cart", "View"), "Cart.View")
    }

    override suspend fun purchaseSuccess(payload: Payload) {
        client.get(prepareUrl(payload, "Purchase", "Success"), "Purchase.Success")
    }
}