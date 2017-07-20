package com.dream.dreamview.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dream.dreamview.util.LogUtil;


/**
 * Created by Administrator on 2017/7/11
 */

public class SwipeBackLayout extends FrameLayout {
    private static final int DEFAULT_SHADOW_WIDTH = 40;
    private static final float DEFAULT_PERCENT = 0.5f;

    private ViewDragHelper viewDragHelper;
    private int mScreenWidth;
    private int currentX;
    private View mParentView;
    private Drawable mLeftShadow;
    private int mShadowWidth;
    private SwipeBackListener mSwipeListener;
    private float mPercent;

    public SwipeBackLayout(Context context) {
        super(context);
        init(context);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        viewDragHelper = ViewDragHelper.create(this, new MyViewDragHelper());
        // 设置左侧边缘滑动
//        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    public void replace(Activity activity) {
        mPercent = DEFAULT_PERCENT;
        mScreenWidth = mScreenWidth > 0 ? mScreenWidth : getScreenWidth();
        ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup content = (ViewGroup) root.getChildAt(0);
        root.removeView(content);
        this.addView(content);
        root.addView(this);
        setContentView(content);
    }

    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public void setShadow(Drawable shadow) {
        this.mLeftShadow = shadow;
        this.mShadowWidth = DEFAULT_SHADOW_WIDTH;
    }

    public void setShadow(Drawable shadow, @IntRange(from = 1) int shadowWidth) {
        this.mLeftShadow = shadow;
        this.mShadowWidth = shadowWidth;
    }

    public void setSwipeListener(SwipeBackListener listener) {
        this.mSwipeListener = listener;
    }

    private void setContentView(View view) {
        this.mParentView = view;
    }

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        if (mLeftShadow != null && mParentView != null) {
            mLeftShadow.setBounds(mParentView.getLeft() - mShadowWidth, 0, mParentView.getLeft(), getHeight());
            mLeftShadow.draw(canvas);
        }
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
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
            return true;
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
            currentX = left;
            LogUtil.e("当前" + currentX);
            return left < 0 ? 0 : left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            LogUtil.e("当前getLeft()值：" + left);

            if (mLeftShadow != null) {
                invalidate();
            }
            if (mSwipeListener != null) {
                if (left >= mScreenWidth + mShadowWidth) {
                    mSwipeListener.back();
                } else if (left <= 0) {
                    mSwipeListener.resume();
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
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
            if (currentX >= mScreenWidth * mPercent) {
                viewDragHelper.settleCapturedViewAt(mScreenWidth + mShadowWidth, 0);
            } else {
                viewDragHelper.settleCapturedViewAt(0, 0);
            }
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
