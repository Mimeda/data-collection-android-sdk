package com.mimeda.mlinkmobile.data

import com.mimeda.mlinkmobile.data.model.Product

object MockData {
    val products = listOf(
        Product(
            id = 1,
            name = "Product 1",
            price = 100.0,
            barcode = 123456,
            quantity = 10,
            description = "Description 1"
        ),
        Product(
            id = 2,
            name = "Product 2",
            price = 200.0,
            barcode = 234567,
            quantity = 20,
            description = "Description 2"
        ),
        Product(
            id = 3,
            name = "Product 3",
            price = 300.0,
            barcode = 345678,
            quantity = 30,
            description = "Description 3"
        ),
    )
}