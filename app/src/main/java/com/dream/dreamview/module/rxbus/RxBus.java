package com.dream.dreamview.module.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created on 2017/8/28.
 */

public class RxBus {
  private final Subject<Object> mBus;

  private RxBus() {
    // toSerialized method made bus thread safe
    mBus = PublishSubject.create().toSerialized();
  }

  private static class Holder {
    private static final RxBus BUS = new RxBus();
  }

  public static RxBus getInstance() {
    return Holder.BUS;
  }

  /*
   * 发送
   */
  public void post(Object obj) {
    mBus.onNext(obj);
  }

  /*
   * 转换为特定类型的Obserbale
   */
  public <T> Observable<T> toObservable(Class<T> tClass) {
    return mBus.ofType(tClass);
  }

  public Observable<Object> toObservable() {
    return mBus;
  }

  /*
   * 是否有Observable订阅
   */
  public boolean hasObservers() {
    return mBus.hasObservers();
  }

}
