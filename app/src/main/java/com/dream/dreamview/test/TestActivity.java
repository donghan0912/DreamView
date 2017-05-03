package com.dream.dreamview.test;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;

/**
 * Created by lenovo on 2017/5/3.
 */

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_activity_test);
        TextView t = (TextView) findViewById(R.id.title);
        t.setVisibility(View.GONE);
        TestFragment testFragment = new TestFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, testFragment).commit();

    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity){
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top==0 ? 60 : rect.top;
    }
}
