package com.dream.dreamview.base;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dream.dreamview.AppConstant;
import com.dream.dreamview.MyApplication;
import com.dream.dreamview.util.LogUtil;


/**
 * Created by Administrator on 2017/7/11
 */

public class BasePreferences {
    private static final boolean DEBUG = AppConstant.DEBUG;
    private final SharedPreferences mPreferences;

    private static class BasePreferencesLoader {
        private static final BasePreferences INSTANCE = new BasePreferences();
    }

    public static BasePreferences getInstance() {
        return BasePreferencesLoader.INSTANCE;
    }

    private BasePreferences() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
    }

    public void setString(String key, String value) {
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        mPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        String value = mPreferences.getString(key, "");
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        return value;
    }

    public void setInt(String key, int value) {
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        mPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        int value = mPreferences.getInt(key, 0);
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        return value;
    }

    public void setLong(String key, long value) {
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        mPreferences.edit().putLong(key, value);
    }

    public long getLong(String key) {
        long value = mPreferences.getLong(key, 0L);
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        return value;
    }

    public void setBoolean(String key, boolean value) {
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        mPreferences.edit().putBoolean(key, value);
    }

    public boolean getBoolean(String key) {
        boolean value = mPreferences.getBoolean(key, false);
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        return value;
    }
}
