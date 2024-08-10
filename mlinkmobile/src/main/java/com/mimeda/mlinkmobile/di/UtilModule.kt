package com.mimeda.mlinkmobile.di

import com.google.gson.Gson

internal object UtilModule {
    val jsonParser by lazy { Gson() }
}