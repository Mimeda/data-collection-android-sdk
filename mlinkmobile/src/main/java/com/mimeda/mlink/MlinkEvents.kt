package com.mimeda.mlink

import com.mimeda.mlink.common.MlinkConstants.ADD_TO_CART
import com.mimeda.mlink.common.MlinkConstants.ADD_TO_FAVORITES
import com.mimeda.mlink.common.MlinkConstants.CART
import com.mimeda.mlink.common.MlinkConstants.HOME
import com.mimeda.mlink.common.MlinkConstants.LISTING
import com.mimeda.mlink.common.MlinkConstants.PRODUCT_DETAILS
import com.mimeda.mlink.common.MlinkConstants.PURCHASE
import com.mimeda.mlink.common.MlinkConstants.SEARCH
import com.mimeda.mlink.common.MlinkConstants.SUCCESS
import com.mimeda.mlink.common.MlinkConstants.VIEW
import com.mimeda.mlink.common.MlinkUrlBuilder
import com.mimeda.mlink.data.MlinkEventPayload
import com.mimeda.mlink.network.MlinkClient

object MlinkEvents {

    object Home {
        /**
         * Sends a view event for the Home page
         */
        suspend fun view(payload: MlinkEventPayload) = apiCall(payload, HOME, VIEW)

        /**
         * Sends an add-to-cart event for the Home page
         */
        suspend fun addToCart(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) showWarning("You Should Send Products")
            apiCall(payload, HOME, ADD_TO_CART)
        }

        /**
         * Sends an add-to-favorites event for the Home page
         */
        suspend fun addToFavorites(payload: MlinkEventPayload) {
            apiCall(payload, HOME, ADD_TO_FAVORITES)
        }
    }

    object Listing {
        /**
         * Sends a view event for the Listing page
         */
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.categoryId.isNullOrEmpty()) showWarning("You Should Send Category ID")
            apiCall(payload, LISTING, VIEW)
        }

        /**
         * Sends an add-to-cart event for the Listing page
         */
        suspend fun addToCart(payload: MlinkEventPayload) {
            when {
                payload.products.isNullOrEmpty() -> showWarning("You Should Send Products")
                payload.categoryId.isNullOrEmpty() -> showWarning("You Should Send Category ID")
            }
            apiCall(payload, LISTING, ADD_TO_CART)
        }

        /**
         * Sends an add-to-favorites event for the Listing page
         */
        suspend fun addToFavorites(payload: MlinkEventPayload) {
            apiCall(payload, LISTING, ADD_TO_FAVORITES)
        }
    }

    object Search {
        /**
         * Sends a view event for the Search page
         */
        suspend fun view(payload: MlinkEventPayload) {
            when {
                payload.keyword.isNullOrEmpty() -> showWarning("You Should Send Keyword")
                payload.totalRowCount == null -> showWarning("You Should Send Total Row Count")
            }
            apiCall(payload, SEARCH, VIEW)
        }

        /**
         * Sends an add-to-cart event for the Search page
         */
        suspend fun addToCart(payload: MlinkEventPayload) {
            when {
                payload.products.isNullOrEmpty() -> showWarning("You Should Send Products")
                payload.keyword.isNullOrEmpty() -> showWarning("You Should Send Keyword")
            }
            apiCall(payload, SEARCH, ADD_TO_CART)
        }

        /**
         * Sends an add-to-favorites event for the Search page
         */
        suspend fun addToFavorites(payload: MlinkEventPayload) {
            apiCall(payload, SEARCH, ADD_TO_FAVORITES)
        }
    }

    object ProductDetails {
        /**
         * Sends a view event for the Product Details page
         */
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) showWarning("You Should Send Products")
            apiCall(payload, PRODUCT_DETAILS, VIEW)
        }

        /**
         * Sends an add-to-cart event for the Product Details page
         */
        suspend fun addToCart(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) showWarning("You Should Send Products")
            apiCall(payload, PRODUCT_DETAILS, ADD_TO_CART)
        }

        /**
         * Sends an add-to-favorites event for the Product Details page
         */
        suspend fun addToFavorites(payload: MlinkEventPayload) {
            apiCall(payload, PRODUCT_DETAILS, ADD_TO_FAVORITES)
        }
    }

    object Cart {
        /**
         * Sends a view event for the Cart page
         */
        suspend fun view(payload: MlinkEventPayload) {
            if (payload.products.isNullOrEmpty()) showWarning("You Should Send Products")
            apiCall(payload, CART, VIEW)
        }
    }

    object Purchase {
        /**
         * Sends a success event for a purchase
         */
        suspend fun success(payload: MlinkEventPayload) {
            when {
                payload.transactionId == null -> showWarning("You Should Send Transaction ID")
                payload.products.isNullOrEmpty() -> showWarning("You Should Send Products")
            }
            apiCall(payload, PURCHASE, SUCCESS)
        }
    }

    /**
     * Logs a warning message if required data is missing
     */
    private fun showWarning(message: String) {
        MlinkLogger.warning("Mlink: $message")
    }

    /**
     * Executes an API call for event tracking
     */
    private suspend fun apiCall(payload: MlinkEventPayload, event: String, eventPage: String) {
        MlinkClient.get(MlinkUrlBuilder.prepareEventUrl(payload, event, eventPage), "$event.$eventPage")
    }
}