package com.dream.dreamview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.width;

/**
 * Created by Administrator on 2017/3/9.
 */

public class SimpleView extends View implements View.OnClickListener{


    private Paint mPaint;
    private Rect mBounds;
    int mCount;

    public SimpleView(Context context) {
        super(context);
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        mPaint = new Paint();
        // 矩形
        mBounds = new Rect();
        setOnClickListener(this);
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = 80;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = 80;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
//        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        int radius = x > y ? y : x;
        canvas.drawCircle(x, y, radius, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(70);
        String s = String.valueOf(mCount);
        mPaint.getTextBounds(s, 0, s.length(), mBounds);
        // 文字矩形宽高
        int width = mBounds.width();
        int height = mBounds.height();
        canvas.drawText(s, getWidth() / 2 - width / 2, getHeight() / 2 + height / 2, mPaint);
    }

    @Override
    public void onClick(View v) {
        mCount++;
        invalidate();
    }
}
