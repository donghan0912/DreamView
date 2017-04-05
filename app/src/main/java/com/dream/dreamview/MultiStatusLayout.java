package com.dream.dreamview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MultiStatusLayout extends FrameLayout {
    private static final int CONTENT_VIEW = -10;
    private static final int LOADING_VIEW = -11;

    private View mContentView;
    private View mCurrentView;
    private int mLoadingLayout;
    private LayoutInflater mInflater;
    private View mLoadingView;
    private int mStatus = CONTENT_VIEW;

    public MultiStatusLayout(@NonNull Context context) {
        this(context, null);
    }

    public MultiStatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiStatusLayout);
        mLoadingLayout = a.getResourceId(R.styleable.MultiStatusLayout_loadingLayout, R.layout.loading_layout);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInflater = LayoutInflater.from(getContext());
        mContentView = getChildAt(0);
        mCurrentView = mContentView;
        showContentView();
    }

    public void showContentView() {
        mStatus = CONTENT_VIEW;
        showViewByStatus(mStatus);
    }

    public void showLoadingView() {
        mStatus = LOADING_VIEW;
        mLoadingView = mInflater.inflate(mLoadingLayout, null);
        mCurrentView = mLoadingView;
        addView(mLoadingView);
        showViewByStatus(mStatus);
    }

    private void showViewByStatus(int status) {
        if (mContentView != null) {
            mContentView.setVisibility(mCurrentView == mContentView ? VISIBLE : GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(mCurrentView == mLoadingView ? VISIBLE : GONE);
        }
    }
}
