package com.dream.dreamview.module.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.dream.dreamview.R;

import java.util.List;


/**
 * Created by Administrator on 2018/6/8.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

  private Context context;
  private List<String> mTitleList;
  private List<Fragment> mFragmentList;

  public TabFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<String> titleList) {
    super(fm);
    this.context = context;
    List<Fragment> tmp = fm.getFragments();
    if (null != tmp && !tmp.isEmpty()) {
      mFragmentList = tmp;
    } else {
      this.mFragmentList = fragmentList;
    }
    this.mTitleList = titleList;
  }


  @Override
  public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  @Override
  public int getCount() {
    return mTitleList.size();
  }



  public View getTabViewPay(int position) {
    View v = LayoutInflater.from(context).inflate(R.layout.tab_classroom_item, null);
    TextView title =  v.findViewById(R.id.tab_classroom_tv);
    title.setText(mTitleList.get(position));
    return v;
  }

}
