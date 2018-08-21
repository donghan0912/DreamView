package com.dream.dreamview.module.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.util.TabLayoutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/8/20.
 */
public class TabActivity extends BaseActivity {

  public static void start(Context context) {
    Intent intent = new Intent(context, TabActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.common_activity_tab);
    TabLayout mTabLayout = findViewById(R.id.tab_layout);
    ViewPager mViewPager = findViewById(R.id.view_pager);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    FirstFragment fragment1 = FirstFragment.getInstance();
    SecondFragment fragment2 = SecondFragment.getInstance();
    fragmentList.add(fragment1);
    fragmentList.add(fragment2);
    titleList.add("测试一");
    titleList.add("测试二");
    TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
    mViewPager.setAdapter(adapter);
    mTabLayout.setupWithViewPager(mViewPager);
    for (int i = 0; i < mTabLayout.getTabCount(); i++) {
      TabLayout.Tab tab = mTabLayout.getTabAt(i);
      if (tab != null) {
        tab.setCustomView(adapter.getTabViewPay(i));
      }
    }
    TabLayoutUtils.repaintTabs(this, mTabLayout);
  }


}
