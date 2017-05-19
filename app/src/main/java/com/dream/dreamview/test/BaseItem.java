package com.dream.dreamview.test;

import android.view.ViewGroup;

import com.dream.dreamview.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/5/19.
 */

public interface BaseItem {
    int getItemViewType();
    BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
    void onBindViewHolder(BaseViewHolder holder, int position);
    BaseItem getItem(int viewType);
}
