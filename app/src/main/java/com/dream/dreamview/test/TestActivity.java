package com.dream.dreamview.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static com.dream.dreamview.R.id.view;

/**
 * Created by lenovo on 2017/7/21
 */

public class TestActivity extends NavBaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.ttt;
//        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        closeSwipe();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        this.mSwipeBackLayout.setSwipeabledView(viewPager);
        final List<View> list = new ArrayList<>();
        View view1 = getLayoutInflater().inflate(R.layout.item_view_pager_fir, null);
        View view2 = getLayoutInflater().inflate(R.layout.item_view_pager_sec, null);
        View view3 = getLayoutInflater().inflate(R.layout.item_view_pager_third, null);
        list.add(view1);
        list.add(view2);
        list.add(view3);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return list.get(position);
            }
        });
    }
}
