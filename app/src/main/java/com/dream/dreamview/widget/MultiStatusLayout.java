package com.dream.dreamview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.dream.dreamview.R;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MultiStatusLayout extends FrameLayout {
    private static final int CONTENT_VIEW = 0x0001;
    private static final int LOADING_VIEW = 0x0003;

    private int mLoadingLayout;
    private int mEmptyLayout;
    private int mErrorLayout;
    private int mLoginLayout;
    private int mNoNetworkLayout;
    private int mTimeOutLayout;
    private LayoutInflater mInflater;
    private View mContentView;
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    private View mLoginView;
    private View mNoNetworView;
    private View mTimeOutView;

    public MultiStatusLayout(@NonNull Context context) {
        this(context, null);
    }

    public MultiStatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiStatusLayout);
        mLoadingLayout = a.getResourceId(R.styleable.MultiStatusLayout_loadingLayout, R.layout.layout_loading);
        mEmptyLayout = a.getResourceId(R.styleable.MultiStatusLayout_emptyLayout, R.layout.layout_empty);
        mErrorLayout = a.getResourceId(R.styleable.MultiStatusLayout_errorLayout, R.layout.layout_error);
        mLoginLayout = a.getResourceId(R.styleable.MultiStatusLayout_loginLayout, R.layout.layout_login);
        mNoNetworkLayout = a.getResourceId(R.styleable.MultiStatusLayout_noNetworkLayout, R.layout.layout_no_network);
        mTimeOutLayout = a.getResourceId(R.styleable.MultiStatusLayout_timeOutLayout, R.layout.layout_time_out);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInflater = LayoutInflater.from(getContext());
        mContentView = getChildAt(0);
        showContentView();
    }

    public void showContentView() {
        showCurrentView(mContentView);
    }

    public View getContentView() {
        return mContentView;
    }

    public void showLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = mInflater.inflate(mLoadingLayout, null);
            addView(mLoadingView);
        }
        showCurrentView(mLoadingView);
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public void showEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = mInflater.inflate(mEmptyLayout, null);
            addView(mEmptyView);
        }
        showCurrentView(mEmptyView);
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void showErrorView() {
        if (mErrorView == null) {
            mErrorView = mInflater.inflate(mErrorLayout, null);
            addView(mErrorView);
        }
        showCurrentView(mErrorView);
    }

    public View getErrorView() {
        return mErrorView;
    }

    public void showLoginView() {
        if (mLoginView == null) {
            mLoginView = mInflater.inflate(mLoginLayout, null);
            addView(mLoginView);
        }
        showCurrentView(mLoginView);
    }

    public View getLoginView() {
        return mLoginView;
    }

    public void showNoNetworkView() {
        if (mNoNetworView == null) {
            mNoNetworView = mInflater.inflate(mNoNetworkLayout, null);
            addView(mNoNetworView);
        }
        showCurrentView(mNoNetworView);
    }

    public View getNoView() {
        return mNoNetworView;
    }

    public void showTimeOutView() {
        if (mTimeOutView == null) {
            mTimeOutView = mInflater.inflate(mTimeOutLayout, null);
            addView(mTimeOutView);
        }
        showCurrentView(mTimeOutView);
    }

    public View getTimeOutView() {
        return mTimeOutView;
    }

    private void showCurrentView(View currentView) {
        if (mContentView != null) {
            mContentView.setVisibility(currentView == mContentView ? VISIBLE : GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(currentView == mLoadingView ? VISIBLE : GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(currentView == mEmptyView ? VISIBLE : GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(currentView == mErrorView ? VISIBLE : GONE);
        }
        if (mLoginView != null) {
            mLoginView.setVisibility(currentView == mLoginView ? VISIBLE : GONE);
        }
        if (mNoNetworView != null) {
            mNoNetworView.setVisibility(currentView == mNoNetworView ? VISIBLE : GONE);
        }
        if (mTimeOutView != null) {
            mTimeOutView.setVisibility(currentView == mTimeOutView ? VISIBLE : GONE);
        }
    }
}
