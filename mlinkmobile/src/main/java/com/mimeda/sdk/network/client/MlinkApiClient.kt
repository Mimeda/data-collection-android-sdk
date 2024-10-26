package com.mimeda.sdk.network.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal abstract class MlinkApiClient {
    protected val networkScope = CoroutineScope(Dispatchers.IO)

    abstract suspend fun get(url: String, eventName: String)
}