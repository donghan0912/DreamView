package com.dream.dreamview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/19.
 */

public class AAA extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa);
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
                    title.setBackgroundColor(getResources().getColor(R.color.blue_500));
                } else {
                    // 中间状态
                    toolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.blue_500));
                    Drawable drawable = getResources().getDrawable(R.color.blue_500);
                    int i = Math.abs(verticalOffset) + 50;
                    if (i <= 255) {
                        drawable.setAlpha(i);
                        title.setBackground(drawable);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            int color = getResources().getColor(R.color.blue_500);
                            int i2 = ColorUtils.setAlphaComponent(color, i);
                            getWindow().setStatusBarColor(i2);
                        }
                    }
                }
            }
        });
    }

}
