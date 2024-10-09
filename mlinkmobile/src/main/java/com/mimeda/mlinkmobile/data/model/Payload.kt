package com.mimeda.mlinkmobile.data.model

data class Payload(
    val userId: Int,
    val lineItems: List<Int>,
    val products: List<PayloadProduct>,
)

data class PayloadProduct(
    val barcode: Int,
    val quantity: Int,
    val price: Double,
)
