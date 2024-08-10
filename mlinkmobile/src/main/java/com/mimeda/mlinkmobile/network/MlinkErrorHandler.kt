package com.mimeda.mlinkmobile.network

import com.mimeda.mlinkmobile.network.model.MlinkBaseResponse

class MlinkErrorHandler {
    fun getError(response: MlinkBaseResponse<*>?): MlinkError {
        response?.mlinkMeta?.let { meta ->
            return if (meta.errorCode == null)
                MlinkError.UnexpectedException()
            else {
                MlinkError.UnexpectedException()
            }
        }
        return MlinkError.UnexpectedException()
    }
}