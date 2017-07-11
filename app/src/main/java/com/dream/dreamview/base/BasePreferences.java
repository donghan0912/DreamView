package com.dream.dreamview.base;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.util.LruCache;

import com.dream.dreamview.AppConstant;
import com.dream.dreamview.MyApplication;
import com.dream.dreamview.util.LogUtil;


/**
 * Created by Administrator on 2017/7/11
 */

public class BasePreferences {
    private static final int CACHE_COUNT = 20;
    private static final boolean DEBUG = AppConstant.DEBUG;
    private final SharedPreferences mPreferences;
    private final LruCache<String, Object> mCache;

    private static class BasePreferencesLoader {
        private static final BasePreferences INSTANCE = new BasePreferences();
    }

    public static BasePreferences getInstance() {
        return BasePreferencesLoader.INSTANCE;
    }

    private BasePreferences() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mCache = new LruCache<>(CACHE_COUNT);
    }

    public void setString(String key, String value) {
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        mCache.put(key, value);
        mPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        Object obj = mCache.get(key);
        if (obj == null) {
            obj = mPreferences.getString(key, "");
        }
        String value = (String) obj;
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        return (String) obj;
    }

    public void setInt(String key, int value) {
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        mCache.put(key, value);
        mPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        Object obj = mCache.get(key);
        if (obj == null) {
            obj = mPreferences.getInt(key, 0);
        }
        int value = (int) obj;
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        return value;
    }

    public void setLong(String key, long value) {
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        mCache.put(key, value);
        mPreferences.edit().putLong(key, value);
    }

    public long getLong(String key) {
        Object obj = mCache.get(key);
        if (obj == null) {
            obj = mPreferences.getLong(key, 0L);
        }
        long value = (long) obj;
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        return value;
    }

    public void setBoolean(String key, boolean value) {
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        mCache.put(key, value);
        mPreferences.edit().putBoolean(key, value);
    }

    public boolean getBoolean(String key) {
        Object obj = mCache.get(key);
        if (obj == null) {
            obj = mPreferences.getBoolean(key, false);
        }
        boolean value = (boolean) obj;
        if (DEBUG) {
            LogUtil.e(key + ":" + value);
        }
        return value;
    }
}
