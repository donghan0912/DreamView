package com.dream.dreamview.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TwoTextItem extends BaseAdapterItem {



    @Override
    public int getItemViewType() {
        return R.layout.test_activity_recyclerview_item_two;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_activity_recyclerview_item_two, parent, false);
        return new BaseViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.content3, "嘿嘿嘿");
    }

    @Override
    public BaseItem getItem(int viewType) {
        return this;
    }
}
