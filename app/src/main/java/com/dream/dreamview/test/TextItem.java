package com.dream.dreamview.test;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TextItem extends MultiBaseItem<String> {


    public TextItem(String s) {
        super(s);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.test_activity_recyclerview_item;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.content, mData);
    }

}
