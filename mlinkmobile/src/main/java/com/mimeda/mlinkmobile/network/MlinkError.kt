package com.mimeda.mlinkmobile.network

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
sealed class MlinkError(
    open val message: String?,
) : Parcelable {

    @Keep
    @Parcelize
    open class UnexpectedException(override val message: String? = "") : MlinkError(message)

    @Keep
    @Parcelize
    class ParseException : UnexpectedException("")

    @Keep
    @Parcelize
    class NotFoundNetworkException : MlinkError("There is no internet connection. Please connect and try again.")
}