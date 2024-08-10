package com.mimeda.mlinkmobile.common

import com.mimeda.mlinkmobile.BuildConfig

fun String.withBaseUrl(): String {
    return BuildConfig.BASE_URL.plus(this)
}