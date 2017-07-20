package com.dream.dreamview.module.anim;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.base.SwipeBackLayout;
import com.dream.dreamview.base.SwipeBackListener;

/**
 * Created by Administrator on 2017/7/17.
 */

public class TestAc extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        SwipeBackLayout swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.replace(this);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.slide_left_shadow);
        swipeBackLayout.setShadow(drawable);
        swipeBackLayout.setSwipeListener(new SwipeBackListener() {
            @Override
            public void back() {
                finish();
            }

            @Override
            public void resume() {

            }
        });
    }
}
