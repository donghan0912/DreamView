package com.dream.dreamview.test;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TwoTextItem extends MultiBaseItem<List<String>> {


    public TwoTextItem(List<String> strings) {
        super(strings);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.test_activity_recyclerview_item_two;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.content3, mData.get(0));
    }

}
