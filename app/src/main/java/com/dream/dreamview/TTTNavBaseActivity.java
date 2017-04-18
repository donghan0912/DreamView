package com.dream.dreamview;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import static com.dream.dreamview.R.id.toolbar;


/**
 * 带Toolbar的基础Activity
 * Created by lenovo on 2017/4/13.
 */

public abstract class TTTNavBaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTitle;
    private ViewStub mToolbarViewStub;

    protected
    @LayoutRes
    int getToolbarView() {
        return R.layout.base_toolbar;
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
            // 去掉Toolbar标题
//            actionBar.setDisplayShowTitleEnabled(false);
//            setDisplayHomeAsUpEnabled(true);
        }
        // 自定义Toolbar 更多按钮
        if (toolbar != null) {
            toolbar.setOverflowIcon(AppCompatResources.getDrawable(this, R.drawable.ic_more));
        }
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbarViewStub.setLayoutResource(getToolbarView());
            View view = mToolbarViewStub.inflate();
            mToolbar = (Toolbar) view.findViewById(toolbar);
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
        mToolbar.setTitle(text);
        setTitleCenter();
    }

    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enable);
        }
    }


    private int titleMarginStart;

    protected void setTitleCenter() {
        if (mToolbar == null) {
            return;
        }
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
