package com.dream.dreamview.base;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/16.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private View mItemView;
    private SparseArray<View> views;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.mItemView = itemView;

    }

    public void setTitle(@IdRes int idRes, CharSequence text) {
        TextView textView = (TextView) mItemView.findViewById(idRes);
        textView.setText(text);
    }
}
