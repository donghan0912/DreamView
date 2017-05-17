package com.dream.dreamview.base;

import android.content.res.Resources.NotFoundException;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    @LayoutRes
    private int mLayoutResId;
    private List<T> mData = new ArrayList<>();

    public BaseRecyclerViewAdapter() {
        this(0,null);
    }

    public BaseRecyclerViewAdapter(List<T> data) {
        this(0, data);
    }

    public BaseRecyclerViewAdapter(@LayoutRes int resource) {
        this(resource, null);
    }

    /**
     *
     * @param resource
     * @param data data source
     */
    public BaseRecyclerViewAdapter(@LayoutRes int resource, @Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        this.mLayoutResId = resource;
    }

    public @LayoutRes int getLayoutId(int viewType) {
        return mLayoutResId;
    }

    public void setData(List<T> data) {
        this.mData.clear();
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resourceId = getLayoutId(viewType);
        if (resourceId <= 0) {
            throw new NotFoundException("Resource ID \"" + resourceId + "\" is not valid, " +
                    "you should override constructor or the method getResourceId()");
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        if (itemView == null) {
            return null;
        }
        return new BaseViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        onBindRecyclerViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public T getItem(int position) {
        if (position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public abstract void onBindRecyclerViewHolder(BaseViewHolder holder, int position);
}
