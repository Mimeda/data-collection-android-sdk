package com.mimeda.mlink.data

internal enum class UrlPath(val value: String) {
    EVENT("/events?"),
    IMPRESSION("/impressions?"),
    CLICK("/clicks?");
}