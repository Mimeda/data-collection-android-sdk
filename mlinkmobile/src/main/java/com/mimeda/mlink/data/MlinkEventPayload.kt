package com.mimeda.mlink.data

/**
 * MlinkEventPayload is a data class representing the payload of an Mlink event.
 * It contains various properties such as user ID, category ID, keyword, transaction ID,
 * total row count, products, line item IDs, and loyalty card.
 */
data class MlinkEventPayload(
    val userId: Int? = null,
    val categoryId: String? = null,
    val keyword: String? = null,
    val transactionId: String? = null,
    val totalRowCount: Int? = null,
    val products: List<MlinkEventProduct>? = null,
    val lineItemIds: List<Int>? = null,
    val loyaltyCard: String? = null,
)