package com.mimeda.mlinkmobile.di

import com.mimeda.mlinkmobile.data.repository.MLinkRepository
import com.mimeda.mlinkmobile.data.repository.MLinkRepositoryImpl
import com.mimeda.mlinkmobile.network.client.MLinkFuelClient

internal object AppModule {
    private val client by lazy { MLinkFuelClient() }
    val mLinkRepository: MLinkRepository by lazy { MLinkRepositoryImpl(client) }
}