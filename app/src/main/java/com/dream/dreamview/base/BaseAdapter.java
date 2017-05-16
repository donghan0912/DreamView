package com.dream.dreamview.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.dreamview.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    @LayoutRes
    private int mResource;
    private List<T> mData = new ArrayList<>();

    // TODO 实现代码自动提示功能
    public BaseAdapter() {
        this(0,null);
    }

    public BaseAdapter(List<T> data) {
        this(0, data);
    }

    public BaseAdapter(@LayoutRes int resource) {
        this(resource, null);
    }

    /**
     *
     * @param resource
     * @param data data source(数据源)
     */
    public BaseAdapter(@LayoutRes int resource, @Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        this.mResource = resource;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mResource <= 0) {
            return null;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mResource, parent, false);
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
        return mData.size();
    }

    public abstract void onBindRecyclerViewHolder(BaseViewHolder holder, int position);
}
