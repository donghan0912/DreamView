package com.dream.dreamview.sample;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;

/**
 * CustomToolbarActivity CustomToolbarFragment
 * 这两个类，是为了解决，Activity 中包含多个Fragment，并且activity中，有toolbar或其它fragment共有的布局
 * 导致，fragment实现android:fitsSystemWindows="true" 失效
 * 注：开发中尽量避免在Activity包含fragment中，toolbar放在Activity层
 *     应尽量放在Fragment层，这样比较容易控制每个fragment，达到解耦的目的
 * Created by lenovo on 2017/5/3.
 */

public class CustomeToolbarSampleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample_activity_custom_toolbar);
        TextView t = (TextView) findViewById(R.id.title);
        t.setVisibility(View.GONE);
        CustomToolbarSapmleFragment testFragment = new CustomToolbarSapmleFragment();
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
