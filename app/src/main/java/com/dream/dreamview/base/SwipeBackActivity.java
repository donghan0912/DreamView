package com.dream.dreamview.base;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.dream.dreamview.R;

/**
 * Created by Administrator on 2017/7/17
 */

public class SwipeBackActivity extends BaseActivity {
    public SwipeBackLayout mSwipeBackLayout;
    // TODO 利用 GestureDetector 手势探测器 监测是否是水平或垂直滑动

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = new SwipeBackLayout(this);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.slide_left_shadow);
        int startColor = ContextCompat.getColor(this, R.color.translucent);
        int endColor = ContextCompat.getColor(this, R.color.transparent);
        mSwipeBackLayout.replace(this)
                .setShadow(drawable)
                .setGradientColor(startColor, endColor)
                .openFullScreenSwipe()
                .setSwipeListener(new SwipeBackListener() {
                    @Override
                    public void back() {
                        finish();
                        Activity nextActivity = ActivityStackManager.getInstance().getStackTopParent();
                        if (nextActivity instanceof SwipeBackActivity) {
                            // 为了滑动返回页面的时候，由于上一个activity 的view缩放了，导致有白边，效果不理想，换成黑色背景
                            // 将上一个activity 的 DecorView 根布局背景设置成黑色(返回之后记得设置成透明)
                            nextActivity.getWindow().getDecorView().setBackgroundResource(R.color.transparent);
                            ((SwipeBackActivity) nextActivity).mSwipeBackLayout.setScaleX(1.0f);
                            ((SwipeBackActivity) nextActivity).mSwipeBackLayout.setScaleY(1.0f);
                        }
                    }

                    @Override
                    public void resume() {
                        Activity nextActivity = ActivityStackManager.getInstance().getStackTopParent();
                        if (nextActivity instanceof SwipeBackActivity) {
                            nextActivity.getWindow().getDecorView().setBackgroundResource(R.color.black);
                            ((SwipeBackActivity) nextActivity).mSwipeBackLayout.setScaleX(1.0f);
                            ((SwipeBackActivity) nextActivity).mSwipeBackLayout.setScaleY(1.0f);
                        }
                    }

                    @Override
                    public void move(int dx) {
                        Activity nextActivity = ActivityStackManager.getInstance().getStackTopParent();
                        if (nextActivity instanceof SwipeBackActivity) {
                            nextActivity.getWindow().getDecorView().setBackgroundResource(R.color.black);
                            float scale = 0.98f + (1 - 0.98f) / 1000 * dx;
                            if (scale <= 1.0f) {
                                ((SwipeBackActivity) nextActivity).mSwipeBackLayout.setScaleX(scale);
                                ((SwipeBackActivity) nextActivity).mSwipeBackLayout.setScaleY(scale);
                            }
                        }
                    }
                });
    }

    @SuppressWarnings("unused")
    protected void openEdgeSwipe() {
        mSwipeBackLayout.openEdgeSwipe();
    }

    @SuppressWarnings("unused")
    protected void openFullScreenSwipe() {
        mSwipeBackLayout.openFullScreenSwipe();
    }

    protected void closeSwipe() {
        mSwipeBackLayout.closeSwipe();
    }

}
