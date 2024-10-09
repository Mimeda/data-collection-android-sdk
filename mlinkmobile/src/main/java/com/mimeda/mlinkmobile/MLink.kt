package com.mimeda.mlinkmobile

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.Keep
import com.mimeda.mlinkmobile.data.model.Payload
import com.mimeda.mlinkmobile.di.AppModule

@Keep
object MLink {

    private val repository = AppModule.mLinkRepository

    private var appPackageName = ""
    private var baseDomain: String? = ""
    private var appId: String? = ""

    fun init(
        context: Context,
        isLogEnabled: Boolean = true,
    ) {
        MLinkLogger.isEnabled = isLogEnabled
        appPackageName = context.packageName
        context.packageManager.getApplicationInfo(appPackageName, PackageManager.GET_META_DATA).apply {
            baseDomain = metaData.getString("com.mlinkmobile.sdk.basedomain")
            appId = metaData.getString("com.mlinkmobile.sdk.appid")
        }
        MLinkLogger.info("MLinkManager initialized successfully.")
    }

    object Publisher {
        object Events {
            object Home {
                suspend fun view(payload: Payload) = repository.homeView(payload)
                suspend fun addToCart(payload: Payload) = repository.homeAddToCart(payload)
            }

            object Listing {
                suspend fun view(payload: Payload) = repository.listingView(payload)
                suspend fun addToCart(payload: Payload) = repository.listingAddToCart(payload)
            }

            object Search {
                suspend fun view(payload: Payload) = repository.searchView(payload)
                suspend fun addToCart(payload: Payload) = repository.searchAddToCart(payload)
            }

            object ProductDetails {
                suspend fun view(payload: Payload) = repository.productDetailsView(payload)
                suspend fun addToCart(payload: Payload) = repository.productDetailsAddToCart(payload)
            }

            object Cart {
                suspend fun view(payload: Payload) = repository.cartView(payload)
            }

            object Purchase {
                suspend fun success(payload: Payload) = repository.purchaseSuccess(payload)
            }
        }
    }
}