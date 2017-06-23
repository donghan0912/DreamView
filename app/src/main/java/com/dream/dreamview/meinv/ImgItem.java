package com.dream.dreamview.meinv;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dream.dreamview.R;
import com.dream.dreamview.util.LogUtil;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

import static com.dream.dreamview.R.id.img;

/**
 * Created by lenovo on 2017/6/21
 */

public class ImgItem extends BaseItem<Gallery> {

    public ImgItem(Gallery gallery) {
        super(gallery);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.item_activity_beauty;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.desc, mData.desc);
        ImageView imageView = holder.findViewById(img);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = mData.height;
        imageView.setLayoutParams(params);
        Glide.with(imageView.getContext()).load(mData.url).into(imageView);
//        Glide.with(imageView.getContext()).load(R.mipmap.luoluo).into(imageView);
    }
}
