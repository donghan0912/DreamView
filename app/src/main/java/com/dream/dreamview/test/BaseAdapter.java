package com.dream.dreamview.test;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dream.dreamview.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class BaseAdapter<T extends BaseAdapterItem> extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<T> mData;
    private int ttt;

    public BaseAdapter(List<T> item) {
        this.mData = item;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = mData.get(ttt).onCreateViewHolder(parent, viewType);
        ttt++;
        return viewHolder;

        // TODO 怎么维护类型
//        for (int i = 0; i < mData.size(); i++) {
//            if (viewType == mData.get(i).getItemViewType()) {
//                return mData.get(i).onCreateViewHolder(parent, viewType);
//            }
//        }
//        throw new RuntimeException("sss");
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
