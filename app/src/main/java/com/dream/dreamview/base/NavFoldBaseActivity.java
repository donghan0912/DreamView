package com.dream.dreamview.base;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.dreamview.R;


/**
 * 带可折叠Toolbar的基础Activity
 * Created by lenovo on 2017/4/13.
 */

public abstract class NavFoldBaseActivity extends SlideBaseActivity {
    private Toolbar mToolbar;
    private ViewStub mToolbarViewStub;

    protected
    @LayoutRes
    int getToolbarView() {
        return R.layout.base_toolbar_fold;
    }

    protected abstract
    @LayoutRes
    int getContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_nav);
        mToolbarViewStub = (ViewStub) findViewById(R.id.toolbar_stub);
        if (mToolbarViewStub != null && getToolbarView() > 0) {
            setupToolbar();
        }
        ViewStub contentViewStub = (ViewStub) findViewById(R.id.content_stub);
        if (contentViewStub != null && getContentView() > 0) {
            contentViewStub.setLayoutResource(getContentView());
            contentViewStub.inflate();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            setDisplayHomeAsUpEnabled(true);
        }
        // 自定义Toolbar 更多按钮
        if (toolbar != null) {
            toolbar.setOverflowIcon(AppCompatResources.getDrawable(this, R.drawable.ic_more));
        }
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (toolbarLayout != null) {
            toolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        }
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbarViewStub.setLayoutResource(getToolbarView());
            View view = mToolbarViewStub.inflate();
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        }
        return mToolbar;
    }

    public void setTitle(@StringRes int resid) {
        if (mToolbar != null) {
            mToolbar.setTitle(resid);
        }
    }

    public void setTitle(CharSequence text) {
        if (mToolbar != null) {
            mToolbar.setTitle(text);
            setTitleCenter();
        }
    }

    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enable);
        }
    }


    private int titleMarginStart;

    protected void setTitleCenter() {
        int childCount = getToolbar().getChildCount();
        // 注意toolbar设置NavigationIcon
        Drawable navigationIcon = mToolbar.getNavigationIcon();
        for (int i = 0; i < childCount; i++) {
            final View view = getToolbar().getChildAt(i);
            if (view instanceof TextView) {
                TextView childTitle = (TextView) view;
                int deviceWidth = getResources().getDisplayMetrics().widthPixels;
                Paint p = childTitle.getPaint();
                int textWidth = (int) p.measureText(childTitle.getText().toString());
                titleMarginStart = (deviceWidth - textWidth) / 2 - mToolbar.getContentInsetLeft();
                if (navigationIcon == null) {
                    mToolbar.setTitleMarginStart(titleMarginStart);
                    break;
                }
            } else if (view instanceof ImageView) {
                CharSequence title = mToolbar.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            int width = view.getWidth();
                            titleMarginStart = titleMarginStart - width;
                            mToolbar.setTitleMarginStart(titleMarginStart);
                        }
                    });
                    break;
                }
            }
        }
    }

}
