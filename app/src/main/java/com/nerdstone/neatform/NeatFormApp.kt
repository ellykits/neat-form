package com.nerdstone.neatform

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class NeatFormApp : Application() {

    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        instance = this
    }
}
