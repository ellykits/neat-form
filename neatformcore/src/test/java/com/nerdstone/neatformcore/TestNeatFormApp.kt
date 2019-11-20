package com.nerdstone.neatformcore

import android.app.Application
import org.robolectric.TestLifecycleApplication
import timber.log.Timber
import java.lang.reflect.Method

class TestNeatFormApp: Application(), TestLifecycleApplication{

    override fun onCreate() {
        setTheme(R.style.CustomTestTheme)
        super.onCreate()
    }
    override fun beforeTest(method: Method?) {
       Timber.i("Testing method: ${method?.name}")
    }

    override fun prepareTest(test: Any?) {
        Timber.i("Prepared test ${test.toString()}")
    }

    override fun afterTest(method: Method?) {
        Timber.i("End of testing method: ${method?.name}")
    }
}