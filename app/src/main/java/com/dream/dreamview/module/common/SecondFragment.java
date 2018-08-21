package com.dream.dreamview.module.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.dreamview.MainActivity;
import com.dream.dreamview.R;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseRecyclerViewAdapter;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

/**
 * Created on 2018/8/20.
 */
public class SecondFragment extends Fragment {

  public static SecondFragment getInstance() {
    return new SecondFragment();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.common_fragment_second, container, false);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

  }

}
