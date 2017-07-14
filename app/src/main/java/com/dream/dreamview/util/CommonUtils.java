package com.dream.dreamview.util;

import android.content.res.Resources;

import com.dream.dreamview.MyApplication;
import com.dream.dreamview.R;

/**
 * Created by Administrator on 2017/7/14
 */

public class CommonUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStatusBarHeight() {
        int result = 0;
        Resources resources = MyApplication.getContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getToolbarHeight() {
        return (int) MyApplication.getContext().getResources().getDimension(R.dimen.toolbar_height);
    }

    public static int getScreenWidth() {
        return  MyApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return  MyApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

}
