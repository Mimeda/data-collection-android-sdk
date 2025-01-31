package com.mimeda.mlink

import com.mimeda.mlink.common.MlinkConstants.ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.CART
import com.mimeda.mlink.common.MlinkConstants.CART_VIEW
import com.mimeda.mlink.common.MlinkConstants.HOME
import com.mimeda.mlink.common.MlinkConstants.HOME_ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.HOME_VIEW
import com.mimeda.mlink.common.MlinkConstants.LISTING
import com.mimeda.mlink.common.MlinkConstants.LISTING_ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.LISTING_VIEW
import com.mimeda.mlink.common.MlinkConstants.PRODUCT_DETAILS
import com.mimeda.mlink.common.MlinkConstants.PRODUCT_DETAILS_ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.PRODUCT_DETAILS_VIEW
import com.mimeda.mlink.common.MlinkConstants.PURCHASE
import com.mimeda.mlink.common.MlinkConstants.PURCHASE_SUCCESS
import com.mimeda.mlink.common.MlinkConstants.SEARCH
import com.mimeda.mlink.common.MlinkConstants.SEARCH_ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.SEARCH_VIEW
import com.mimeda.mlink.common.MlinkConstants.SUCCESS
import com.mimeda.mlink.common.MlinkConstants.VIEW
import com.mimeda.mlink.common.MlinkUrlBuilder
import com.mimeda.mlink.data.MlinkEventPayload
import com.mimeda.mlink.network.MlinkClient

object MlinkEvents {

    object Home {
        suspend fun view(payload: MlinkEventPayload) = apiCall(payload, HOME, VIEW, HOME_VIEW)

        suspend fun addToCart(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) showWarning("You Should Send Products")
            apiCall(payload, HOME, ADD_TO_CART, HOME_ADD_TO_CART)
        }
    }

    object Listing {
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.categoryId.isNullOrEmpty()) showWarning("You Should Send Category ID")
            apiCall(payload, LISTING, VIEW, LISTING_VIEW)
        }

        suspend fun addToCart(payload: MlinkEventPayload) {
            when {
                payload.products.isNullOrEmpty() -> showWarning("You Should Send Products")
                payload.categoryId.isNullOrEmpty() -> showWarning("You Should Send Category ID")
            }
            apiCall(payload, LISTING, ADD_TO_CART, LISTING_ADD_TO_CART)
        }
    }

    object Search {
        suspend fun view(payload: MlinkEventPayload) {
            when {
                payload.keyword.isNullOrEmpty() -> showWarning("You Should Send Keyword")
                payload.totalRowCount == null -> showWarning("You Should Send Total Row Count")
            }
            apiCall(payload, SEARCH, VIEW, SEARCH_VIEW)
        }

        suspend fun addToCart(payload: MlinkEventPayload) {
            when {
                payload.products.isNullOrEmpty() -> showWarning("You Should Send Products")
                payload.keyword.isNullOrEmpty() -> showWarning("You Should Send Keyword")
            }
            apiCall(payload, SEARCH, ADD_TO_CART, SEARCH_ADD_TO_CART)
        }
    }

    object ProductDetails {
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) showWarning("You Should Send Products")
            apiCall(payload, PRODUCT_DETAILS, VIEW, PRODUCT_DETAILS_VIEW)
        }

        suspend fun addToCart(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) showWarning("You Should Send Products")
            apiCall(payload, PRODUCT_DETAILS, ADD_TO_CART, PRODUCT_DETAILS_ADD_TO_CART)
        }
    }

    object Cart {
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) showWarning("You Should Send Products")
            apiCall(payload, CART, VIEW, CART_VIEW)
        }
    }

    object Purchase {
        suspend fun success(payload: MlinkEventPayload) {
            when {
                payload.transactionId == null -> showWarning("You Should Send Transaction ID")
                payload.products.isNullOrEmpty() -> showWarning("You Should Send Products")
            }
            apiCall(payload, PURCHASE, SUCCESS, PURCHASE_SUCCESS)
        }
    }

    private fun showWarning(message: String) {
        MlinkLogger.warning("Mlink: $message")
    }

    private suspend fun apiCall(payload: MlinkEventPayload, event: String, eventPage: String, eventType: String) {
        MlinkClient.get(MlinkUrlBuilder.prepareEventUrl(payload, event, eventPage), eventType)
    }
}