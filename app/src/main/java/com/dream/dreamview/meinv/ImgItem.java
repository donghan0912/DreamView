package com.dream.dreamview.meinv;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dream.dreamview.R;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

/**
 * Created by lenovo on 2017/6/21.
 */

public class ImgItem extends BaseItem {

    @Override
    public int getLayoutResource() {
        return R.layout.item_activity_beauty;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ImageView imageView = holder.findViewById(R.id.img);
        Glide.with(imageView.getContext()).load(R.mipmap.luoluo).into(imageView);
    }
}
