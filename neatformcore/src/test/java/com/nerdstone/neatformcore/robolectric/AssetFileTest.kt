package com.nerdstone.neatformcore.robolectric

import android.content.Context
import com.nerdstone.neatformcore.TestConstants
import com.nerdstone.neatformcore.datasource.AssetFile
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class AssetFileTest {

    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.systemContext
        compositeDisposable = CompositeDisposable()
    }

    @After
    fun tearDown() {
        compositeDisposable!!.clear()
    }

    @Test
    fun testReadFile() {
        val testScheduler = TestScheduler()
        val testObserver = AssetFile()
            .readAssetFileAsString(context, TestConstants.SAMPLE_ONE_FORM)
            .subscribeOn(testScheduler)
            .observeOn(testScheduler)
            .test()

        testObserver.assertSubscribed()
            .assertNoErrors()
            .dispose()
    }
}
