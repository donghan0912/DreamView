package com.dream.dreamview.meinv.bean;

import com.dream.dreamview.R;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SecItem extends BaseItem<String> {

    public SecItem(String str) {
        super(str);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.sec_item;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.setText(R.id.text, mData);
    }
}
