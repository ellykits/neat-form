package com.nerdstone.neatform;

import android.app.Application;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class NeatFromApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    if(BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    }
  }
}
