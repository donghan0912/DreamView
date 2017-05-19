package com.dream.dreamview.test;

import android.util.SparseArray;

/**
 * Created by Administrator on 2017/5/19.
 */

public abstract class BaseAdapterItem implements BaseItem {
    public SparseArray<BaseItem> items = new SparseArray<>();

    private void init(int type, BaseAdapterItem item) {
        items.put(type, item);
    }
}
