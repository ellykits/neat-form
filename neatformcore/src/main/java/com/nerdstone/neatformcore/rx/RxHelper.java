package com.nerdstone.neatformcore.rx;

import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {

  public static <T> SingleTransformer<T, T> getSingleIoScheduler() {
    return upstream -> upstream.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
