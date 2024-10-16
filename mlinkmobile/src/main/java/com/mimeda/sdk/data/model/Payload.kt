package com.mimeda.sdk.data.model

data class Payload(
    val userId: Int,
    val lineItems: List<Int>,
    val products: List<MlinkEventProduct>? = null,
)

data class MlinkEventProduct(
    val barcode: Int,
    val quantity: Int,
    val price: Double,
)
