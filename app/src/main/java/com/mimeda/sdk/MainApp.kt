package com.mimeda.sdk

import android.app.Application

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Mlink.initialize(
            context = this,
            appId = 1,
            publisher = "Mimeda",
            isLogEnabled = true,
        )
    }
}