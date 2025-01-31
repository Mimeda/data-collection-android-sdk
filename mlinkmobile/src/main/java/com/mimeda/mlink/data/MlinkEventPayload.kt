package com.mimeda.mlink.data

data class MlinkEventPayload(
    val userId: Int? = null,
    val categoryId: String? = null,
    val keyword: String? = null,
    val transactionId: String? = null,
    val totalRowCount: Int? = null,
    val products: List<MlinkEventProduct>? = null,
    val lineItemIds: List<Int>? = null,
)