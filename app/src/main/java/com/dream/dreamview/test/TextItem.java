package com.dream.dreamview.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TextItem extends BaseAdapterItem {

    public TextItem getItem(int type) {
        return this;
    }

    @Override
    public int getItemViewType() {
        return R.layout.test_activity_recyclerview_item;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_activity_recyclerview_item, parent, false);
        return new BaseViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.content, "hahaha");
    }
}
