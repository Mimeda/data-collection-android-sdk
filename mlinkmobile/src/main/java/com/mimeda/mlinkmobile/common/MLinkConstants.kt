package com.mimeda.mlinkmobile.common

internal object MLinkConstants {

    object Endpoint {
        const val INIT = "init"
        const val SAMPLE = "sample"
        const val BOOKS = "all_books"
    }

    object HeaderKey {
        const val SDK_VERSION = "X-MMS-Sdk-Version"
        const val TIMESTAMP = "X-MMS-Req-Timestamp"
        const val KEY = "X-MMS-Key"
    }
}