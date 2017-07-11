package com.dream.dreamview;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.dream.dreamview.base.BasePreferences;
import com.dream.dreamview.module.common.CommonPreferences;
import com.dream.dreamview.util.LogUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static android.R.attr.width;


/**
 * Created by Administrator on 2017/3/28.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

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

    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        String processName = getProcessName();
        // 每新建一个进程Application 的onCreate(）方法都将被调用一次
        if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) {//判断进程名，保证只有主进程运行
            //主进程初始化逻辑
            LogUtil.d("我被调用了");
            instance = this;

            int width = getResources().getDisplayMetrics().widthPixels;
            CommonPreferences.setScreenWidth(width);

        }

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

}

