package com.dream.dreamview.module.anim;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dream.dreamview.module.common.CommonPreferences;
import com.dream.dreamview.util.CommonUtils;

/**
 * Created by Administrator on 2017/7/14.
 */

public class TestView extends View {

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int lastX = 0;
    private int lastY = 0;
    private int normalX = 0;
    private int normalY = 0;
    private static final int screenWidth = CommonPreferences.getScreenWidth();
    //屏幕宽度
    private static final int screenHeight = CommonUtils.getScreenHeight() - CommonUtils.getToolbarHeight() - CommonUtils.getStatusBarHeight();
    //屏幕高度

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                normalX = lastX = (int) event.getRawX();
                normalY = lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getRawX() - lastX);
                int dy = (int) (event.getRawY() - lastY);
                int left = getLeft() + dx;
                int top = getTop();
                int right = getRight() + dx;
                int bottom = getBottom() ;
                if(left < 0){
                    left = 0;
                    right = left + getWidth();
                }
//                if(right > screenWidth){
//                    right = screenWidth;
//                    left = right - getWidth();
//                }
                if(top < 0){
                    top = 0;
                    bottom = top + getHeight();
                }
                if(bottom > screenHeight){
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                if (callBack != null) {
                    callBack.Move((int) (event.getRawX() - normalX));
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
    private CallBack callBack;
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    interface CallBack {
        void Move(int dx);
    }
}
