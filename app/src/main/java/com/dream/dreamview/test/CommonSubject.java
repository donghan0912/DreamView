package com.dream.dreamview.test;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;

/**
 * Created by Administrator on 2017/6/19.
 */

public class CommonSubject extends Subject<RetrofitActivity.BaseRes> {
    @Override
    public boolean hasObservers() {
        return false;
    }

    @Override
    public boolean hasThrowable() {
        return false;
    }

    @Override
    public boolean hasComplete() {
        return false;
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }

    @Override
    protected void subscribeActual(Observer<? super RetrofitActivity.BaseRes> observer) {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(RetrofitActivity.BaseRes value) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
