package com.nerdstone.neatformcore.robolectric;

import android.content.Context;
import com.nerdstone.neatformcore.TestConstants;
import com.nerdstone.neatformcore.datasource.AssetFile;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

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
    TestObserver<String> testObserver = new AssetFile()
        .readAssetFileAsString(context, TestConstants.SAMPLE_ONE_FORM)
        .subscribeOn(testScheduler)
        .observeOn(testScheduler)
        .test();

    testObserver.assertSubscribed()
        .assertNoErrors()
        .dispose();
  }
}
