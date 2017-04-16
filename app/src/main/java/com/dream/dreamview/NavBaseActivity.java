package com.dream.dreamview;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

/**
 * 带Toolbar的基础Activity
 * Created by lenovo on 2017/4/13.
 */

public abstract class NavBaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTitle;

    protected @LayoutRes int getToolbarView() {
        return R.layout.base_toolbar;
    }

    protected abstract @LayoutRes int getContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_nav);
        ViewStub toolbarViewStub = (ViewStub) findViewById(R.id.toolbar_stub);
        if (toolbarViewStub != null && getToolbarView() > 0) {
            setupToolbar(toolbarViewStub);
        }
        ViewStub contentViewStub = (ViewStub) findViewById(R.id.content_stub);
        if (contentViewStub != null && getContentView() > 0) {
            contentViewStub.setLayoutResource(getContentView());
            contentViewStub.inflate();
        }
    }

    private void setupToolbar(ViewStub viewStub) {
        Toolbar toolbar = getToolbar(viewStub);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 去掉Toolbar标题
            actionBar.setDisplayShowTitleEnabled(false);
            setDisplayHomeAsUpEnabled(true);
        }
        // 自定义Toolbar 更多按钮
        toolbar.setOverflowIcon(AppCompatResources.getDrawable(this, R.drawable.ic_more));
    }

    private Toolbar getToolbar(ViewStub viewStub) {
        if (mToolbar == null) {
            viewStub.setLayoutResource(getToolbarView());
            View view = viewStub.inflate();
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
