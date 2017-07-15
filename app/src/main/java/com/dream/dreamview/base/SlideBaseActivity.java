package com.dream.dreamview.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.dream.dreamview.R;
import com.dream.dreamview.module.common.CommonPreferences;
import com.dream.dreamview.util.LogUtil;

/**
 * https://github.com/NashLegend/SwipetoFinishActivity
 * 滑动关闭页面基类
 */
public class SlideBaseActivity extends BaseActivity {

    public SwipeLayout swipeLayout;

    /**
     * 是否开启滑动关闭页面功能
     * true 开启 false 关闭
     */
    protected boolean slideEnabled = true;

    /**
     * false 左侧边缘滑动关闭
     * true 任意位置右滑关闭
     */
    protected boolean LeftSlideEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeLayout = new SwipeLayout(this);
    }

    public void setLeftSlideEnabled(boolean LeftSlideEnabled) {
        this.LeftSlideEnabled = LeftSlideEnabled;
    }

    public void setSlideEnabled(boolean slideEnabled) {
        this.slideEnabled = slideEnabled;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        int screenWidth = CommonPreferences.getScreenWidth();
        int width = screenWidth > 0 ? screenWidth : getScreenWidth();
        swipeLayout.replaceLayer(this, width);
    }

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        CommonPreferences.setScreenWidth(width);
        return width;
    }

    private boolean swipeFinished = false;

    @Override
    public void finish() {
        if (swipeFinished) {
            // 滑动返回触发
            super.finish();
            overridePendingTransition(0, 0);
        } else {
            // 返回按钮触发
            swipeLayout.cancelPotentialAnimation();
            super.finish();
            overridePendingTransition(0, R.anim.slide_out_right);
        }
    }

    class SwipeLayout extends FrameLayout {

        //private View backgroundLayer;用来设置滑动时的背景色
        private Drawable leftShadow;

        public SwipeLayout(Context context) {
            super(context);
        }

        public SwipeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void replaceLayer(Activity activity, int width) {
            leftShadow = activity.getResources().getDrawable(R.drawable.slide_left_shadow);
            touchSlop = (int) (touchSlopDP * activity.getResources().getDisplayMetrics().density);
            sideWidth = (int) (sideWidthInDP * activity.getResources().getDisplayMetrics().density);
            mActivity = activity;
            screenWidth = width;
            setClickable(true);
            final ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
            content = root.getChildAt(0);
            ViewGroup.LayoutParams params = content.getLayoutParams();
            ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(-1, -1);
            root.removeView(content);
            this.addView(content, params2);
            root.addView(this, params);
        }

        @Override
        protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
            boolean result = super.drawChild(canvas, child, drawingTime);
            final int shadowWidth = leftShadow.getIntrinsicWidth();
            int left = (int) (getContentX()) - shadowWidth;
            leftShadow.setBounds(left, child.getTop(), left + shadowWidth, child.getBottom());
            leftShadow.draw(canvas);
            return result;
        }

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

        @Override
        public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
            if (slideEnabled && !canSwipe && !ignoreSwipe) {
                if (LeftSlideEnabled) {
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            downX = ev.getX();
                            downY = ev.getY();
                            currentX = downX;
                            currentY = downY;
                            lastX = downX;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float dx = ev.getX() - downX;
                            float dy = ev.getY() - downY;
                            if (dx * dx + dy * dy > touchSlop * touchSlop) {
                                if (dy == 0f || Math.abs(dx / dy) > 1) {
                                    downX = ev.getX();
                                    downY = ev.getY();
                                    currentX = downX;
                                    currentY = downY;
                                    lastX = downX;
                                    canSwipe = true;
                                    tracker = VelocityTracker.obtain();
                                    return true;
                                } else {
                                    ignoreSwipe = true;
                                }
                            }
                            break;
                    }
                } else if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getX() < sideWidth) {
                    canSwipe = true;
                    tracker = VelocityTracker.obtain();
                    return true;
                }
            }
            if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
                ignoreSwipe = false;
            }
            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return canSwipe || super.onInterceptTouchEvent(ev);
        }

        boolean hasIgnoreFirstMove;

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            if (canSwipe) {
                tracker.addMovement(event);
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        currentX = downX;
                        currentY = downY;
                        lastX = downX;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // TODO 将上一个activity 的根view 缩放
                        Activity nextActivity = ActivityStackManager.getInstance().getStackTopParent();
                        ((SlideBaseActivity)nextActivity).swipeLayout.setScaleX(0.95f);
                        ((SlideBaseActivity)nextActivity).swipeLayout.setScaleY(0.95f);

                        currentX = event.getX();
                        currentY = event.getY();
                        float dx = currentX - lastX;
                        if (dx != 0f && !hasIgnoreFirstMove) {
                            hasIgnoreFirstMove = true;
                            dx = dx / dx;
                        }
                        if (getContentX() + dx < 0) {
                            setContentX(0);
                        } else {
                            setContentX(getContentX() + dx);
                        }
                        lastX = currentX;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // TODO 将上一个activity 的根view 恢复正常大小
                        Activity nextActivity2 = ActivityStackManager.getInstance().getStackTopParent();
                        ((SlideBaseActivity)nextActivity2).swipeLayout.setScaleX(1.0f);
                        ((SlideBaseActivity)nextActivity2).swipeLayout.setScaleY(1.0f);

                        tracker.computeCurrentVelocity(10000);
                        tracker.computeCurrentVelocity(1000, 20000);
                        canSwipe = false;
                        hasIgnoreFirstMove = false;
                        int mv = screenWidth * 3;
                        if (Math.abs(tracker.getXVelocity()) > mv) {
                            animateFromVelocity(tracker.getXVelocity());
                        } else {
                            if (getContentX() > screenWidth / 3) {
                                animateFinish(false);
                            } else {
                                animateBack(false);
                            }
                        }
                        tracker.recycle();
                        break;
                    default:
                        break;
                }
            }
            return super.onTouchEvent(event);
        }

        ObjectAnimator animator;

        public void cancelPotentialAnimation() {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }

        public void setContentX(float x) {
            int ix = (int) x;
            content.setX(ix);
            invalidate();
        }

        public float getContentX() {
            return content.getX();
        }


        /**
         * 弹回，不关闭，因为left是0，所以setX和setTranslationX效果是一样的
         *
         * @param withVel 使用计算出来的时间
         */
        private void animateBack(boolean withVel) {
            cancelPotentialAnimation();
            animator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), 0);
            int tmpDuration = withVel ? ((int) (duration * getContentX() / screenWidth)) : duration;
            if (tmpDuration < 100) {
                tmpDuration = 100;
            }
            animator.setDuration(tmpDuration);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        }

        private void animateFinish(boolean withVel) {
            cancelPotentialAnimation();
            animator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), screenWidth);
            int tmpDuration = withVel ? ((int) (duration * (screenWidth - getContentX()) / screenWidth)) : duration;
            if (tmpDuration < 100) {
                tmpDuration = 100;
            }
            animator.setDuration(tmpDuration);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!mActivity.isFinishing()) {
                        swipeFinished = true;
                        mActivity.finish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }
            });
            animator.start();
        }

        private final int duration = 200;

        private void animateFromVelocity(float v) {
            if (v > 0) {
                if (getContentX() < screenWidth / 3 && v * duration / 1000 + getContentX() < screenWidth / 3) {
                    animateBack(false);
                } else {
                    animateFinish(true);
                }
            } else {
                if (getContentX() > screenWidth / 3 && v * duration / 1000 + getContentX() > screenWidth / 3) {
                    animateFinish(false);
                } else {
                    animateBack(true);
                }
            }

        }
    }

}
