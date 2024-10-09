package com.mimeda.mlinkmobile

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MLink.init(
            context = this,
            isLogEnabled = true
        )
    }
}