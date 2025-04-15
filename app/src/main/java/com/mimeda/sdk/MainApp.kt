package com.mimeda.sdk

import android.app.Application
import com.mimeda.mlink.Mlink

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Mlink.initialize(
            context = this,
            app = "migros-sanalmarket",
            isLogEnabled = true,
        )
    }
}