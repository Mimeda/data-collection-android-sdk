package com.mimeda.mlink.data

/**
 * Represents the different URL paths used for API requests.
 */
internal enum class UrlPath(val value: String) {
    EVENT("/events?"),
    IMPRESSION("/impressions?"),
    CLICK("/clicks?");
}