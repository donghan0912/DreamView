package com.dream.dreamview.base;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
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
        views = new SparseArray<>();
        this.mItemView = itemView;
    }

    /**
     * return view by id
     * @param id
     * @param <T>
     * @return
     */
    public  <T extends View> T findViewById(@IdRes int id) {
        View view = views.get(id);
        if (view == null) {
            view = mItemView.findViewById(id);
            if (view == null) {
                throw new NullPointerException("can not find view by id");
            }
            views.put(id, view);
        }
        return (T) view;
    }

    public void setText(@IdRes int id, CharSequence text) {
        TextView textView = findViewById(id);
        textView.setText(text);
    }

    public void setText(@IdRes int id, @StringRes int resid) {
        TextView textView = findViewById(id);
        textView.setText(resid);
    }

}
