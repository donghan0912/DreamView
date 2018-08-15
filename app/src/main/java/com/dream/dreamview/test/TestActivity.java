package com.dream.dreamview.test;

import android.arch.persistence.room.EmptyResultSetException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.module.room.dao.User;
import com.dream.dreamview.module.room.dao.UserModel;
import com.dream.dreamview.util.AssetsHelper;
import com.dream.dreamview.util.FileHelper;
import com.dream.dreamview.util.LogUtil;
import com.dream.dreamview.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/7/21
 */

public class TestActivity extends NavBaseActivity {

    private ViewPager viewPager;
    private Button btn;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    protected int getContentView() {
        return R.layout.ttt;
//        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ttt);
//        closeSwipe();
        viewPager = findViewById(R.id.view_pager);
       /* btn = findViewById(R.id.btn);
        final CircleProgress circleProgress = findViewById(R.id.circle);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgress.setProgress(50);
            }
        });*/

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[] location = new int[2];
        viewPager.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        int x = location[0];
        int y = location[1];
        int height = viewPager.getHeight();
        int width = viewPager.getWidth();
        this.mSwipeBackLayout.setUnInterceptPos(x, y, x + width, y + height);

//        int[] location2 = new int[2];
//        btn.getLocationInWindow(location2); //获取在当前窗口内的绝对坐标
//        int x2 = location2[0];
//        int y2 = location2[1];
//        int height2 = btn.getHeight();
//        int width2 = btn.getWidth();
//        this.mSwipeBackLayout.setUnInterceptPos(x2, y2, x2 + width2, y2 + height2);
    }



    @Override
    protected void onPause() {
        super.onPause();
        // clear all the subscriptions
        mDisposable.clear();
    }

}
