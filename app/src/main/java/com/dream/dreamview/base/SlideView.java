package com.dream.dreamview.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.dream.dreamview.R;


/**
 * Created by Administrator on 2017/7/11
 */

class SlideView extends FrameLayout {

    //private View backgroundLayer;用来设置滑动时的背景色
    private Drawable leftShadow;

    boolean canSwipe = false;
    /**
     * 超过了touchslop仍然没有达到没有条件，则忽略以后的动作
     */
    boolean ignoreSwipe = false;
    View content;
    Activity mActivity;
    int sideWidthInDP = 16;
    int sideWidth = 72;
    int screenWidth = 1080;
    VelocityTracker tracker;

    float downX;
    float downY;
    float lastX;
    float currentX;
    float currentY;

    int touchSlopDP = 20;
    int touchSlop = 60;

    public SlideView(Context context) {
        super(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



}
