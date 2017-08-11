package com.dream.dreamview.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.dream.dreamview.R;

/**
 * Created by dh on 11/6/14
 */
public class CircleProgress extends View {
    private static final int STYLE_FIRST = 1;
    private static final int STYLE_SECOND = 2;
    private static final int STYLE_THIRD = 3;

    private Paint paint;
    private Paint bgPaint;
    protected Paint textPaint;
    private RectF rectF = new RectF();
    private float strokeWidth;
    private float suffixTextSize;
    private int suffixTextColor;
    private String bottomText;
    private float textSize;
    private int textColor;
    private float progress = 0;
    private float max;
    private float arcAngle;
    private String suffixText = "%";
    private float suffixTextPadding;

    private final int default_text_color = Color.rgb(66, 145, 241);
    private final float default_suffix_text_size;
    private final float default_suffix_padding;

    private final float default_stroke_width;
    private final String default_suffix_text;
    private float default_text_size;
    //    private final int min_size;
    private int mCenterX;
    private int mCenterY;
    private int[] mArcColors = {Color.RED, Color.GREEN, Color.BLUE};
    private int type;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = sp2px(18);
        default_text_size = sp2px(40);
        default_suffix_text_size = sp2px(15);
        default_suffix_padding = dp2px(4);
        default_suffix_text = "%";
        default_stroke_width = dp2px(5);
        initByAttributes(context, attrs);
        initPainters();
        startAnima(0, progress / max);
    }

    protected void initByAttributes(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);
        type = attributes.getInt(R.styleable.CircleProgress_content_style, 1);
        textColor = attributes.getColor(R.styleable.CircleProgress_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.CircleProgress_text_size, default_text_size);
        arcAngle = attributes.getFloat(R.styleable.CircleProgress_angle, 360);
        max = attributes.getFloat(R.styleable.CircleProgress_max, 100);
        progress = attributes.getFloat(R.styleable.CircleProgress_progress, 0);
        strokeWidth = attributes.getDimension(R.styleable.CircleProgress_stroke_width, default_stroke_width);
        suffixTextSize = attributes.getDimension(R.styleable.CircleProgress_suffix_text_size, default_suffix_text_size);
        suffixTextColor = attributes.getColor(R.styleable.CircleProgress_suffix_text_color, default_text_color);
        suffixText = TextUtils.isEmpty(attributes.getString(R.styleable.CircleProgress_suffix_text)) ? default_suffix_text : attributes.getString(R.styleable.CircleProgress_suffix_text);
        suffixTextPadding = attributes.getDimension(R.styleable.CircleProgress_suffix_text_padding, default_suffix_padding);
//        bottomTextSize = attributes.getDimension(R.styleable.CircleProgress_bottom_text_size, default_bottom_text_size);
        bottomText = attributes.getString(R.styleable.CircleProgress_bottom_text);
        int arcColors = attributes.getResourceId(R.styleable.CircleProgress_arc_colors, 0);
        if (arcColors != 0) {
            int[] colors = getResources().getIntArray(arcColors);
            if (colors.length == 0) {//如果渐变色为数组为0，则尝试以单色读取色值
                int color = getResources().getColor(arcColors);
                mArcColors = new int[2];
                mArcColors[0] = color;
                mArcColors[1] = color;
            } else if (colors.length == 1) {//如果渐变数组只有一种颜色，默认设为两种相同颜色
                mArcColors = new int[2];
                mArcColors[0] = colors[0];
                mArcColors[1] = colors[0];
            } else {
                mArcColors = colors;
            }
        }
        attributes.recycle();
    }

    protected void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeWidth(strokeWidth - 1);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @SuppressWarnings("unused")
    public float getStrokeWidth() {
        return strokeWidth;
    }

    @SuppressWarnings("unused")
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public float getSuffixTextSize() {
        return suffixTextSize;
    }

    @SuppressWarnings("unused")
    public void setSuffixTextSize(float suffixTextSize) {
        this.suffixTextSize = suffixTextSize;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public String getBottomText() {
        return bottomText;
    }

    @SuppressWarnings("unused")
    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        this.invalidate();
    }

    public int getProgress() {
        return (int) progress;
    }

    public void setProgress(int progress) {
        float start = this.progress / max;
        this.progress = Math.abs(progress);
        if (this.progress > max) {
            this.progress %= max;
        }
        startAnima(start, progress / max);
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public float getArcAngle() {
        return arcAngle;
    }

    @SuppressWarnings("unused")
    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public String getSuffixText() {
        return suffixText;
    }

    @SuppressWarnings("unused")
    public void setSuffixText(String suffixText) {
        this.suffixText = suffixText;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public float getSuffixTextPadding() {
        return suffixTextPadding;
    }

    @SuppressWarnings("unused")
    public void setSuffixTextPadding(float suffixTextPadding) {
        this.suffixTextPadding = suffixTextPadding;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mCenterX = width / 2;
        mCenterY = height / 2;
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, height - strokeWidth / 2f);
//        float radius = width / 2f;
//        float angle = (360 - arcAngle) / 2f;
//        arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        drawCircle(canvas);
        // 这里是半圆时，展示文字
//        if (arcBottomHeight == 0) {
//            float radius = getWidth() / 2f;
//            float angle = (360 - arcAngle) / 2f;
//            arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
//        }
//
//        if (!TextUtils.isEmpty(getBottomText())) {
//            textPaint.setTextSize(bottomTextSize);
//            float bottomTextBaseline = getHeight() - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
//            canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
//        }
    }

    private void drawCircle(Canvas canvas) {
        if (type == STYLE_THIRD) {
            canvas.drawArc(rectF, 0, 360, false, paint);
            return;
        }
        canvas.rotate(-90, mCenterX, mCenterY);
        paint.setShader(new SweepGradient(mCenterX, mCenterY, mArcColors, null));
        float startAngle = 8;
        float finishedSweepAngle = progress / max * arcAngle;
        float finishedStartAngle = finishedSweepAngle + startAngle;
        canvas.drawArc(rectF, finishedStartAngle, arcAngle - finishedSweepAngle, false, bgPaint);
        canvas.drawArc(rectF, startAngle, finishedSweepAngle, false, paint);
    }

    private void drawText(Canvas canvas) {
        String text = String.valueOf(getProgress());
        if (!TextUtils.isEmpty(text)) {
            if (type == STYLE_FIRST) { // 百分比
                textPaint.setColor(textColor);
                textPaint.setTextSize(textSize);
                float textHeight = textPaint.descent() + textPaint.ascent();
                float textBaseline = (getHeight() - textHeight) / 2.0f;
                float textWidth = textPaint.measureText(text);
                canvas.drawText(text, (getWidth() - textWidth) / 2.0f, textBaseline, textPaint);
                textPaint.setTextSize(suffixTextSize);
                canvas.drawText(suffixText, mCenterX + textWidth / 2.0f + suffixTextPadding, textBaseline, textPaint);
            } else if (type == STYLE_SECOND) {
                // text 是已学习整数，不是百分比进度
                // suffixTextColor 是进度颜色，textColor是总进度颜色
                // suffixTextPadding 为间距
                textPaint.setColor(suffixTextColor);
                textPaint.setTextSize(textSize);
                float textWidth = textPaint.measureText(text + suffixText + (int) max);
                canvas.drawText(text, (getWidth() - textWidth) / 2.0f, mCenterY - suffixTextPadding / 2, textPaint);
                textPaint.setColor(textColor);
                canvas.drawText(suffixText + (int) max, mCenterX + textPaint.measureText(text) - textWidth / 2.0f, mCenterY - suffixTextPadding / 2, textPaint);
                textPaint.setColor(suffixTextColor);
                textPaint.setTextSize(suffixTextSize);
                float textHeight = textPaint.descent() + textPaint.ascent();
                int percent = (int) (progress * 100 / max);
                String s = percent + "%";
                canvas.drawText(s, mCenterX - textPaint.measureText(s) / 2.0f, mCenterY - textHeight + suffixTextPadding / 2, textPaint);
            } else if (type == STYLE_THIRD) {
                textPaint.setColor(textColor);
                textPaint.setTextSize(textSize);
                float textHeight = textPaint.descent() + textPaint.ascent();
                float textBaseline = (getHeight() - textHeight) / 2.0f;
                float textWidth = textPaint.measureText(text + suffixText) + suffixTextPadding;
                canvas.drawText(text, (getWidth() - textWidth) / 2.0f, textBaseline, textPaint);
                textPaint.setTextSize(suffixTextSize);
                canvas.drawText(suffixText, mCenterX - textWidth / 2.0f + textPaint.measureText(text) + suffixTextPadding, textBaseline, textPaint);
            }
        }
    }

    private void startAnima(float start, float end) {
        if (type == STYLE_THIRD) {
            return;
        }
        ValueAnimator mAnimator = ValueAnimator.ofFloat(start, end);
        mAnimator.setDuration(1000);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                progress = (int) (f * max);
                invalidate();
            }
        });
        mAnimator.start();
    }

    public int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public float sp2px(float sp){
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}