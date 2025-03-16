package com.mimeda.mlink.data

/**
 * MlinkAdPayload is a data class representing the payload for Mlink ad requests.
 * It contains information about the user, product, and ad creative.
 */
data class MlinkAdPayload(
    val userId: Int? = null,
    val productSku: String? = null,
    val lineItemId: Int? = null,
    val creativeId: Int? = null,
    val adUnit: String? = null,
    val keyword: String? = null,
    val payload: String? = null,
)
