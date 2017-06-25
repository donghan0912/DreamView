package com.dream.dreamview;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by Administrator on 2017/3/28.
 */

public class DreamViewApplication extends Application {

    public static RefWatcher getRefWatcher(Context context) {
        DreamViewApplication application = (DreamViewApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }
}
