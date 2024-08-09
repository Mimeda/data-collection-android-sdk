package com.mimeda.mlinkmobile.network

import com.google.gson.Gson

internal object UtilModule {
    val jsonParser by lazy { Gson() }
}