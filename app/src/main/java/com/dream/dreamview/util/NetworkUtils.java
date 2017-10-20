package com.dream.dreamview.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dream.dreamview.MyApplication;

/**
 * Created on 2017/10/20.
 */

public class NetworkUtils {
    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 1;
    /**
     * 移动网络
     */
    public static final int NETWORKTYPE_MOBILE = 2;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 3;


    public static int getNetworkType() {
        int mNetWorkType = NETWORKTYPE_MOBILE;
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                mNetWorkType = NETWORKTYPE_MOBILE;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }

}
