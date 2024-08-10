package com.mimeda.mlinkmobile.common

import com.mimeda.mlinkmobile.network.MlinkError

sealed class MlinkResource<out T : Any> {
    data class Success<out T : Any>(val data: T) : MlinkResource<T>()
    data class Error(val error: MlinkError) : MlinkResource<Nothing>()
}