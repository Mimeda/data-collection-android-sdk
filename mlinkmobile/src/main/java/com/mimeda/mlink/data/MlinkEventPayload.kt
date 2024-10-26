package com.mimeda.mlink.data

data class MlinkEventPayload(
    val userId: Int? = null,
    val adIDList: List<Int>? = null,
    val products: List<MlinkEventProduct>? = null,
)

data class MlinkEventProduct(
    val barcode: Int,
    val quantity: Int,
    val price: Double,
)
