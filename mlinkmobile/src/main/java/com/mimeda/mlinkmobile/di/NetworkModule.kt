package com.mimeda.mlinkmobile.di

import com.mimeda.mlinkmobile.network.client.CustomFuelClient

internal object NetworkModule {
    val client by lazy { CustomFuelClient() }
}