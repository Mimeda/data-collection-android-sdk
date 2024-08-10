package com.mimeda.mlinkmobile.di

import com.google.gson.Gson

internal object MlinkUtilModule {
    val jsonParser by lazy { Gson() }
}