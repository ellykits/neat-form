package com.opensrp.neatformcore.robolectric;

import android.content.Context;

import com.opensrp.neatformcore.TestConstants;
import com.opensrp.neatformcore.datasource.AssetFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

@RunWith(RobolectricTestRunner.class)
public class AssetFileTest {
    private CompositeDisposable compositeDisposable;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.systemContext;
        compositeDisposable = new CompositeDisposable();
    }

    @After
    public void tearDown() {
        compositeDisposable.clear();
    }

    @Test
    public void testReadFile() {
        TestScheduler testScheduler = new TestScheduler();
        TestObserver<String> testObserver = new AssetFile().readFile(context, TestConstants.SAMPLE_ONE_FORM)
                .subscribeOn(testScheduler)
                .observeOn(testScheduler)
                .test();

        testObserver.assertSubscribed()
                .assertNoErrors()
                .dispose();
    }
}
