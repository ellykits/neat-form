package com.nerdstone.neatformcore.robolectric.datasource

import android.content.Context
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.TestConstants
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class `Test working with Assets` {

    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var context: Context

    @Before
    fun `Before everything else`() {
        context = RuntimeEnvironment.systemContext
        compositeDisposable = CompositeDisposable()
    }

    @Test
    fun `Read file should work well`() {
        val testScheduler = TestScheduler()
        val testObserver = AssetFile()
            .readAssetFileAsString(context, TestConstants.SAMPLE_ONE_FORM_FILE)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertSubscribed()
            .assertNoErrors()
            .dispose()
    }


    @After
    fun `After everything else`() {
        compositeDisposable!!.clear()
    }
}
