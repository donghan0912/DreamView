package com.dream.dreamview.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dream.dreamview.R;
import com.dream.dreamview.util.LogUtil;


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


    private ViewDragHelper viewDragHelper;
    private TextView parentView;

    public SlideView(Context context) {
        super(context);
        init();
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new MyViewDragHelper());
        // 设置左侧边缘滑动
//        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        parentView = (TextView) getChildAt(0);
//        parentView.setClickable(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean interceptTouchEvent = viewDragHelper.shouldInterceptTouchEvent(ev);
        LogUtil.e("是否拦截事件：" + interceptTouchEvent);
        return interceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    class MyViewDragHelper extends ViewDragHelper.Callback {

        /**
         * 指定 哪些子元素可以移动
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // return true 所有子元素都可以拖拽移动
            return child == parentView;
        }

        /**
         *
         * @param child 滑动的子view
         * @param left x轴方向的移动位置(相对于原始位置，往左滑，该值为负，右滑，值为正)
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            LogUtil.e("=============" + left + "/" + dx);
            return left;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
//            viewDragHelper.captureChildView(parentView, pointerId);
        }

        /**
         *
         * @param releasedChild
         * @param xvel 水平方向的速度 向右为正
         * @param yvel 垂直方向的速度，向下为正
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }
    }
}
