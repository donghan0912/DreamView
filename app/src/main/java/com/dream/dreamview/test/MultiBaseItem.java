package com.dream.dreamview.test;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.dreamview.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/5/19.
 */

public abstract class MultiBaseItem<T> implements MultiItem {
    public T mData;

    public MultiBaseItem(@NonNull T t) {
        this.mData = t;
    }

    public void setData(T t) {
        this.mData = t;
    }

    @Override
    public int getItemViewType() {
        return getLayoutResource();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resourceId = getLayoutResource();
        if (resourceId <= 0) {
            throw new Resources.NotFoundException("Resource ID \"" + resourceId + "\" is not valid");
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        if (itemView == null) {
            return null;
        }
        return new BaseViewHolder(itemView, viewType);
    }
}
