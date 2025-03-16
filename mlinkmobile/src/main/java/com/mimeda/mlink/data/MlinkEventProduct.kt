package com.mimeda.mlink.data

/**
 * MlinkEventProduct is a data class representing a product in an
 * Mlink event, containing its barcode, quantity, and price.
 */
data class MlinkEventProduct(
    val barcode: Int,
    val quantity: Int,
    val price: Double,
)