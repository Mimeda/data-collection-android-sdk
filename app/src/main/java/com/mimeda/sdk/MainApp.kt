package com.mimeda.sdk

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MLink.init(
            context = this,
            appId = 1,
            publisher = "Mimeda",
            isLogEnabled = true,
        )
    }
}