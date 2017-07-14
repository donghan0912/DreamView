package com.dream.dreamview.module.common;

import com.dream.dreamview.base.BasePreferences;

/**
 * Created by Administrator on 2017/7/11.
 */

public class CommonPreferences {
    public static final String KEY_SCREEN_WIDTH = "key_screen_width";
    public static final String KEY_SCREEN_HEIGHT = "key_screen_height";

    public static void setScreenWidth(int value) {
        BasePreferences.getInstance().setInt(KEY_SCREEN_WIDTH, value);
    }

    public static int getScreenWidth() {
        return BasePreferences.getInstance().getInt(KEY_SCREEN_WIDTH);
    }
}
