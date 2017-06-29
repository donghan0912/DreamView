package com.dream.dreamview.module.multi.api;

import com.dream.dreamview.R;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

/**
 * Created by Administrator on 2017/6/27.
 */

public class FirstItem extends BaseItem<String> {

    public FirstItem(String str) {
        super(str);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.first_item;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.setText(R.id.text, mData);
    }
}
