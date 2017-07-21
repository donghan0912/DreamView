package com.dream.dreamview.module.anim;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.dream.dreamview.R;
import com.dream.dreamview.base.ActivityStackManager;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.base.SwipeBackLayout;
import com.dream.dreamview.base.SwipeBackListener;

/**
 * Created by Administrator on 2017/7/17
 */

public class SwipeBackActivity extends BaseActivity {

    public SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.replace(this);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.slide_left_shadow);
        swipeBackLayout.setShadow(drawable);
        int color1 = ContextCompat.getColor(this, R.color.translucent);
        int color2 = ContextCompat.getColor(this, R.color.transparent);
        swipeBackLayout.setGradientColor(color1, color2);
        swipeBackLayout.setSwipeListener(new SwipeBackListener() {
            @Override
            public void back() {
                finish();
                Activity nextActivity = ActivityStackManager.getInstance().getStackTopParent();
                if (nextActivity instanceof SwipeBackActivity) {
                    // 为了滑动返回页面的时候，由于上一个activity 的view缩放了，导致有白边，效果不理想，换成黑色背景
                    // 将上一个activity 的 DecorView 根布局背景设置成黑色(返回之后记得设置成透明)
                    nextActivity.getWindow().getDecorView().setBackgroundResource(R.color.transparent);
                    ((SwipeBackActivity) nextActivity).swipeBackLayout.setScaleX(1.0f);
                    ((SwipeBackActivity) nextActivity).swipeBackLayout.setScaleY(1.0f);
                }
            }

            @Override
            public void resume() {
                Activity nextActivity = ActivityStackManager.getInstance().getStackTopParent();
                if (nextActivity instanceof SwipeBackActivity) {
                    nextActivity.getWindow().getDecorView().setBackgroundResource(R.color.black);
                    ((SwipeBackActivity) nextActivity).swipeBackLayout.setScaleX(1.0f);
                    ((SwipeBackActivity) nextActivity).swipeBackLayout.setScaleY(1.0f);
                }
            }

            @Override
            public void move(int dx) {
                Activity nextActivity = ActivityStackManager.getInstance().getStackTopParent();
                if (nextActivity instanceof SwipeBackActivity) {
                    nextActivity.getWindow().getDecorView().setBackgroundResource(R.color.black);
                    float scale = 0.98f + (1 - 0.98f) / 1000 * dx;
                    if (scale <= 1.0f) {
                        ((SwipeBackActivity) nextActivity).swipeBackLayout.setScaleX(scale);
                        ((SwipeBackActivity) nextActivity).swipeBackLayout.setScaleY(scale);
                    }
                }
            }
        });
    }

    @SuppressWarnings("unused")
    protected void setEdgeEnabled(boolean enabled) {
        swipeBackLayout.setEdgeEnabled(enabled);
    }

    @SuppressWarnings("unused")
    protected void setFullScreenEnabled(boolean enabled) {
        swipeBackLayout.setFullScreenEnabled(enabled);
    }

    protected void setSwipeEnabled(boolean enabled) {
        swipeBackLayout.setSwipeEnabled(enabled);
    }
}
