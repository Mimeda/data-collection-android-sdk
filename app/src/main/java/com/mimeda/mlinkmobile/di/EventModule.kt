package com.mimeda.mlinkmobile.di

import com.mimeda.mlinkmobile.network.MlinkEvent
import com.mimeda.mlinkmobile.network.MlinkEventImpl
import com.mimeda.mlinkmobile.network.client.MlinkApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {

    @Provides
    @Singleton
    fun provideMlinkEventManager(): MlinkEvent = MlinkEventImpl()
}