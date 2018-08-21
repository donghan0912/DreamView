package com.dream.dreamview.module.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.dreamview.R;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

/**
 * Created on 2018/8/20.
 */
public class FirstFragment extends Fragment {

  public static FirstFragment getInstance() {
    return new FirstFragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.common_fragment_first, container, false);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

    BaseRecyclerViewAdapter<MainItem> adapter = new BaseRecyclerViewAdapter<>();
    String[] datas = {"多状态页面", "测试1", "测试2", "测试3", "自定义toolbar", "Retrofit"
            , "瀑布流", "多类型RecyclerView", "视频", "视频列表", "动画", "页面全局滑动返回，手势冲突"
            , "Kotlin", "数据库RoomDB", "加解密", "原生WebView实现网页选中同步"
            , "腾讯内核WebView实现网页选中同步", "lottie动画", "自定义下拉刷新", "时间选择器", "TabLayout"};
    for (String data : datas) {
      MainItem mainItem = new MainItem(data);
      adapter.addData(mainItem);
    }
    recyclerView.setAdapter(adapter);
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
