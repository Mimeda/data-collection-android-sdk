package com.mimeda.mlinkmobile.network

enum class HeaderKey(val key: String) {
    SDK_VERSION("X-MMS-Sdk-Version"),
    TIMESTAMP("X-MMS-Req-Timestamp"),
    KEY("X-MMS-Key")
}