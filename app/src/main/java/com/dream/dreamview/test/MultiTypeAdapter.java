package com.dream.dreamview.test;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dream.dreamview.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class MultiTypeAdapter<T extends MultiBaseItem> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> mData;

    public MultiTypeAdapter() {
        this(null);
    }

    public MultiTypeAdapter(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    public void setData(List<T> data) {
        this.mData.clear();
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < mData.size(); i++) {
            if (viewType == mData.get(i).getItemViewType()) {
                return mData.get(i).onCreateViewHolder(parent, viewType);
            }
        }
        throw new RuntimeException("no viewType valid");
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
