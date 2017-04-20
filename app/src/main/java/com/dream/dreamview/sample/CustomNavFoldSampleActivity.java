package com.dream.dreamview.sample;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.widget.TextView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;

/**
 * Created by Administrator on 2017/4/19.
 */

public class CustomNavFoldSampleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_custom_nav_fold_activity);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        final TextView title = (TextView) findViewById(R.id.title);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) { // 展开状态
                    title.setBackgroundColor(Color.TRANSPARENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(Color.TRANSPARENT);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    // 折叠状态
                    title.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                } else {
                    // 中间状态
                    int toolbarColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
                    int statusbarColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
                    int i = Math.abs(verticalOffset) + 50;
                    if (i <= 255) {
                        title.setBackgroundColor(ColorUtils.setAlphaComponent(toolbarColor, i));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(statusbarColor, i));
                        }
                    }
                }
            }
        });
    }

}
