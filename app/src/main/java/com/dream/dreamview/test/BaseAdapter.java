package com.dream.dreamview.test;

import android.content.res.Resources.NotFoundException;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.dreamview.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class BaseAdapter<T extends BaseAdapterItem> extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<T> mData;

    public BaseAdapter(List<T> item) {
        this.mData = item;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < mData.size(); i++) {
            if (viewType == mData.get(i).getItemViewType()) {
                return mData.get(i).onCreateViewHolder(parent, viewType);
            }
        }
        throw new RuntimeException("no view type");

        // TODO 怎么维护类型
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        this.mData.get(position).onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {

        return this.mData.get(position).getItemViewType();
    }
}
