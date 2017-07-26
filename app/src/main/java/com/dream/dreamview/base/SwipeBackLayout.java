package com.dream.dreamview.base;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
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
    private static final int DEFAULT_SHADOW_WIDTH = 26;
    private static final float DEFAULT_PERCENT = 0.5f;

    private ViewDragHelper viewDragHelper;
    private int mScreenWidth;
    private int currentX;
    private View mContentView;
    private Drawable mLeftShadow;
    private int mShadowWidth;
    private SwipeBackListener mSwipeListener;
    private float mPercent;
    private ArgbEvaluator mEvaluator;
    private int mStartColor;
    private int mEndColor;
    private boolean mBgEnabled;
    private boolean mEdgeEnabled;
    private boolean mFullScreenEnabled;
    private float lastX = 0;
    private float lastY = 0;
    private boolean isVertical;
    private boolean isHorizontal;


    public SwipeBackLayout(Context context) {
        super(context);
        init();
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, new SwipeViewDragHelper());
    }

    public SwipeBackLayout replace(Activity activity) {
        mPercent = DEFAULT_PERCENT;
        mScreenWidth = mScreenWidth > 0 ? mScreenWidth : getScreenWidth();
        ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup content = (ViewGroup) root.getChildAt(0);
        root.removeView(content);
        this.addView(content);
        root.addView(this);
        setContentView(content);
        return this;
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public SwipeBackLayout setShadow(Drawable shadow) {
        this.mLeftShadow = shadow;
        this.mShadowWidth = DEFAULT_SHADOW_WIDTH;
        return this;
    }

    @SuppressWarnings("unused")
    public SwipeBackLayout setShadow(Drawable shadow, @IntRange(from = 1) int shadowWidth) {
        this.mLeftShadow = shadow;
        this.mShadowWidth = shadowWidth;
        return this;
    }

    public void setSwipeListener(SwipeBackListener listener) {
        this.mSwipeListener = listener;
    }

    private void setContentView(View view) {
        this.mContentView = view;
    }

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        if (mLeftShadow != null && mContentView != null) {
            mLeftShadow.setBounds(mContentView.getLeft() - mShadowWidth, 0, mContentView.getLeft(), getHeight());
            mLeftShadow.draw(canvas);
        }
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            viewDragHelper.abort();
        }
        if (action == MotionEvent.ACTION_MOVE) {
            if (isHorizontal) {
                return viewDragHelper.shouldInterceptTouchEvent(event);
            }
            if (isVertical) {
                return false;
            }
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(event.getX() - lastX);
                float dy = Math.abs(event.getY() - lastY);
                if (dx > 0 && dx * 0.5f > dy) {// 水平
                    isHorizontal = true;
                    lastX = event.getX();
                    lastY = event.getY();
                } else if (dy > 0) {// 竖直
                    isVertical = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isHorizontal = false;
                isVertical = false;
                break;
        }
        return viewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            if (isHorizontal) {
                viewDragHelper.processTouchEvent(event);
                return true;
            }
            if (isVertical) {
                return false;
            }
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(event.getX() - lastX);
                float dy = Math.abs(event.getY() - lastY);
                if (dx > 0 && dx * 0.5f > dy) {// 水平
                    isHorizontal = true;
                    lastX = event.getX();
                    lastY = event.getY();
                    viewDragHelper.processTouchEvent(event);
                } else if (dy > 0) {// 竖直
                    isVertical = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                viewDragHelper.processTouchEvent(event);
                isHorizontal = false;
                isVertical = false;
                break;
        }
        return true;
    }

    private class SwipeViewDragHelper extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return !(mEdgeEnabled || !mFullScreenEnabled);
        }

        /**
         * @param child 滑动的子view
         * @param left  x轴方向的移动位置(相对于原始位置，往左滑，该值为负，右滑，值为正)
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            currentX = left;
            LogUtil.e("当前: " + currentX);
            return left < 0 ? 0 : left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            LogUtil.e("当前getLeft()值：" + left + "/" + dx);

            if (mLeftShadow != null) {
                invalidate();
            }
            if (mSwipeListener != null) {
                if (left >= mScreenWidth) {
                    mSwipeListener.back();
                } else if (left <= 0) {
                    mSwipeListener.resume();
                }
                mSwipeListener.move(left);
            }
            refreshBackgroundColor(left);
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
            if (mEdgeEnabled) {
                viewDragHelper.captureChildView(mContentView, pointerId);
            }
        }

        /**
         * @param xvel 水平方向的速度 向右为正
         * @param yvel 垂直方向的速度，向下为正
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (currentX >= mScreenWidth * mPercent || xvel > 200) {
                viewDragHelper.settleCapturedViewAt(mScreenWidth, 0);
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

    private void refreshBackgroundColor(float x) {
        if (!mBgEnabled) {
            return;
        }
        if (mEvaluator == null) {
            mEvaluator = new ArgbEvaluator();
        }
        int evaluate = (int) mEvaluator.evaluate(x / mScreenWidth, mStartColor, mEndColor);
        this.setBackgroundColor(evaluate);
    }

    public SwipeBackLayout setGradientColor(@ColorInt int startColor, @ColorInt int endColor) {
        this.mBgEnabled = true;
        this.mStartColor = startColor;
        this.mEndColor = endColor;
        return this;
    }

    public SwipeBackLayout openEdgeSwipe() {
        this.mEdgeEnabled = true;
        this.mFullScreenEnabled = false;
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        return this;
    }

    public SwipeBackLayout openFullScreenSwipe() {
        this.mFullScreenEnabled = true;
        this.mEdgeEnabled = false;
        return this;
    }

    public SwipeBackLayout closeSwipe() {
        this.mEdgeEnabled = false;
        this.mFullScreenEnabled = false;
        return this;
    }
}
