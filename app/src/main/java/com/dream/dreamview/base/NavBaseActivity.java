package com.dream.dreamview.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import com.dream.dreamview.R;
import com.dream.dreamview.util.CommonUtils;

/**
 * 带Toolbar的基础Activity
 * Created by lenovo on 2017/4/13.
 */

public abstract class NavBaseActivity extends SwipeBackActivity {
    private Toolbar mToolbar;
    private TextView mTitle;
    private ViewStub mToolbarViewStub;

    // 可通过重写该方法，自定义toolbar layout
    // 然后通过findViewById 获取自定义layout中的控件
    // 注意：自定义layout中toolbar的id，应尽量和当前基类中的id保持一致，避免重新设置toolbar
    /**
     * 如果不需要toolbar,可重写该方法，retrun 0 即可
     **/
    protected
    @LayoutRes
    int getToolbarLayout() {
        return R.layout.base_toolbar;
    }

    protected abstract
    @LayoutRes
    int getContentView();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_nav);
        mToolbarViewStub = (ViewStub) findViewById(R.id.toolbar_stub);
        if (mToolbarViewStub != null && getToolbarLayout() > 0) {
            setupToolbar();
        }
        if (getContentView() > 0) {
            ViewStub contentViewStub = (ViewStub) findViewById(R.id.content_stub);
            if (contentViewStub != null) {
                contentViewStub.setLayoutResource(getContentView());
                contentViewStub.inflate();
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // 自定义Toolbar 更多按钮
            toolbar.setOverflowIcon(AppCompatResources.getDrawable(this, R.drawable.ic_more));
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 去掉Toolbar标题
            actionBar.setDisplayShowTitleEnabled(false);
            setDisplayHomeAsUpEnabled(false);
        }
        // TODO 当Toolbar设置 android:fitsSystemWindows="true"之后，
        // TODO 1. 如果设置状态栏透明，toolbar会跑到状态栏下面，需要重新设置toolbar的paddingTop，需执行下面该方法
        // TODO 1. 如果状态栏有颜色，非透明，系统会默认给toolbar设置paddingTop,不用执行下面该方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int paddingLeft = mToolbar.getPaddingLeft();
            int paddingTop = mToolbar.getPaddingTop();
            int paddingRight = mToolbar.getPaddingRight();
            int paddingBottom = mToolbar.getPaddingBottom();
            int statusBarHeight = CommonUtils.getStatusBarHeight();
            paddingTop += statusBarHeight;
            mToolbar.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbarViewStub.setLayoutResource(getToolbarLayout());
            View view = mToolbarViewStub.inflate();
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
            mTitle = (TextView) view.findViewById(R.id.toolbar_title);
        }
        return mToolbar;
    }

    public void setTitle(@StringRes int resid) {
        if (mTitle != null) {
            mTitle.setText(resid);
        }
    }

    public void setTitle(CharSequence text) {
        if (mTitle != null) {
            mTitle.setText(text);
        }
    }

    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enable);
        }
    }
}
