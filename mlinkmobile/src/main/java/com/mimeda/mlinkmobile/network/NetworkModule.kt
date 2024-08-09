package com.mimeda.mlinkmobile.network

internal object NetworkModule {
    val client by lazy { CustomFuelClient() }
}