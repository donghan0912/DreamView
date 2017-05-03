package com.dream.dreamview.test;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.dreamview.R;

/**
 * Created by lenovo on 2017/5/3.
 */

public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment_test, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 实现思路：设置AppBarLayout的折叠最小高度：状态栏高度 + title高度
        // 设置title的marginTop 值为状态栏高度
        int statusBarHeight = getStatusBarHeight(getActivity());
        final TextView title = (TextView) view.findViewById(R.id.title);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) title.getLayoutParams();
        params.setMargins(0, statusBarHeight, 0, 0);
        title.setLayoutParams(params);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        float scale = getActivity().getResources().getDisplayMetrics().density;
        int height = (int) (44 * scale + 0.5f);
        toolbarLayout.setMinimumHeight(height + statusBarHeight);

        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) { // 展开状态
                    title.setBackgroundColor(Color.TRANSPARENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    // 折叠状态
                    title.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                    }
                } else {
                    // 中间状态
                    int toolbarColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
                    int statusbarColor = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
                    int i = Math.abs(verticalOffset) + 50;
                    if (i <= 255) {
                        title.setBackgroundColor(ColorUtils.setAlphaComponent(toolbarColor, i));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getActivity().getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(statusbarColor, i));
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity){
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top==0 ? 60 : rect.top;
    }
}
