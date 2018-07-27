package com.dream.dreamview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.dream.dreamview.base.ActivityStackManager;
import com.dream.dreamview.base.BasePreferences;
import com.dream.dreamview.module.common.CommonPreferences;
import com.dream.dreamview.util.LogUtil;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static android.R.attr.width;


/**
 * Created by Administrator on 2017/3/28.
 */

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks{
    private static MyApplication instance;
    private RefWatcher refWatcher;

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        refWatcher = LeakCanary.install(this);
        String processName = getProcessName();
        instance = this;
        // 每新建一个进程Application 的onCreate(）方法都将被调用一次
        if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) {//判断进程名，保证只有主进程运行
            //主进程初始化逻辑
            LogUtil.d("我被调用了");
            registerActivityLifecycleCallbacks(this);
        }
        // App 首次就可以加载 x5 内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                LogUtil.e(b + "========================================");
            }
        });
    }

    public String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityStackManager.getInstance().addStack(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityStackManager.getInstance().removeStack(activity);
    }

    /**
     * 完全退出应用.
     */
    public void finishAllActivity() {
        if (AppConstant.DEBUG) {
            LogUtil.d("AppExit.");
        }

        ActivityStackManager.getInstance().finishAllActivity();
    }
}

