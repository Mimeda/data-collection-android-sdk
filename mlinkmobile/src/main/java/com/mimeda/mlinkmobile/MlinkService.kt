package com.mimeda.mlinkmobile

import retrofit2.Response
import retrofit2.http.POST

internal interface MlinkService {
    @POST("endpoint")
    suspend fun sendProduct(
        productId: String,
    ): Response<Boolean>
}