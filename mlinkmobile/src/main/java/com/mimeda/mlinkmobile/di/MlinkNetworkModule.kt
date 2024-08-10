package com.mimeda.mlinkmobile.di

import com.mimeda.mlinkmobile.network.client.MlinkFuelClient

internal object MlinkNetworkModule {
    val client by lazy { MlinkFuelClient() }
}