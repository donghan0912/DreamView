package com.dream.dreamview.base;

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

/**
 * 带Toolbar的基础Activity
 * Created by lenovo on 2017/4/13.
 */

public abstract class NavBaseActivity extends BaseActivity {
    private Toolbar mToolbar;
    private TextView mTitle;
    private ViewStub mToolbarViewStub;

    protected @LayoutRes int getToolbarView() {
        return R.layout.base_toolbar;
    }

    protected @LayoutRes int getContentView() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_nav);
        mToolbarViewStub = (ViewStub) findViewById(R.id.toolbar_stub);
        if (mToolbarViewStub != null && getToolbarView() > 0) {
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
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbarViewStub.setLayoutResource(getToolbarView());
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
