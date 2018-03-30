package com.dream.dreamview.module.anim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.dream.dreamview.MainActivity;
import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.widget.SwipeRefreshLayout;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2018/3/22.
 */

public class CustomRefreshActivity extends BaseActivity {

  public static void start(Context context) {
    context.startActivity(new Intent(context, CustomRefreshActivity.class));
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.anim_custom_refresh_activity);
    final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_layout);
//    swipeRefreshLayout.setRefreshing(true);
    RecyclerView recyclerView = findViewById(R.id.recycler_view);

    BaseRecyclerViewAdapter<MainItem> adapter = new BaseRecyclerViewAdapter<>();
    String[] datas = {"多状态页面", "测试1", "测试2", "测试3", "自定义toolbar", "Retrofit"
            , "瀑布流", "多类型RecyclerView", "视频", "视频列表", "动画", "页面全局滑动返回，手势冲突"
            , "Kotlin", "数据库RoomDB", "加解密", "原生WebView实现网页选中同步"
            , "腾讯内核WebView实现网页选中同步", "lottie动画", "自定义下拉刷新"};
    for (String data : datas) {
      MainItem mainItem = new MainItem(data);
      adapter.addData(mainItem);
    }
    recyclerView.setAdapter(adapter);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        Observable.timer(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
          @Override
          public void accept(Long aLong) throws Exception {
            swipeRefreshLayout.setRefreshing(false);
          }
        });
      }
    });

  }

  class MainItem extends BaseItem<String> {

    MainItem(String s) {
      this.mData = s;
    }

    @Override
    public int getLayoutResource() {
      return R.layout.main_item;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
      baseViewHolder.setText(R.id.text, mData);
    }
  }

}
