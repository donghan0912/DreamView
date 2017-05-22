package com.dream.dreamview.test;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.dream.dreamview.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/5/19.
 */

public interface MultiItem {
    @LayoutRes
    int getLayoutResource();
    int getItemViewType();
    BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
    void onBindViewHolder(BaseViewHolder holder, int position);
}
