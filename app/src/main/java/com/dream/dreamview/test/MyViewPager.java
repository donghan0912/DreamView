package com.dream.dreamview.test;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dream.dreamview.util.LogUtil;

/**
 * Created by Administrator on 2017/7/25.
 */

public class MyViewPager extends ViewPager {


    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e("拦截：按下" );
                return false;

            case MotionEvent.ACTION_MOVE:
                LogUtil.e("拦截：滑动" );
                return true;
            case MotionEvent.ACTION_UP:
                LogUtil.e("拦截：抬起" );
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e("处理：按下" );
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e("处理：滑动" );
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.e("处理：抬起" );
                break;
        }
        return true;
    }
}
