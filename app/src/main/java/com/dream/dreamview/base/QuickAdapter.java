package com.dream.dreamview.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/5/16.
 */

public abstract class QuickAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    @LayoutRes
    private int mLayoutId;

    public QuickAdapter(@LayoutRes int layId) {
        this.mLayoutId = layId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutId <= 0) {
            return null;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        if (itemView == null) {
            return null;
        }
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        onBindRecyclerViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public abstract void onBindRecyclerViewHolder(BaseViewHolder holder, int position);
}
