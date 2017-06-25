package com.dream.dreamview.meinv;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.dreamview.R;
import com.dream.dreamview.util.LogUtil;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;


/**
 * Created by lenovo on 2017/6/21
 */

public class ImgItem extends BaseItem<Gallery> {
    private int selectPos1 = -1;
    private int selectPos2 = -1;

    public ImgItem(Gallery gallery) {
        super(gallery);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.item_activity_beauty;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        holder.setText(R.id.desc, mData.desc);
        final ImageView imageView = holder.findViewById(R.id.img);
        final TextView textView = holder.findViewById(R.id.desc);
        final CheckBox like = holder.findViewById(R.id.like);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = mData.height;
        imageView.setLayoutParams(params);
        Glide.with(imageView.getContext()).load(mData.url).into(imageView);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LargeActivity.start(imageView.getContext(), mData.largeUrl);
//            }
//        });
        if (selectPos1 == position) {
            textView.setTextColor(ContextCompat.getColor(imageView.getContext(), R.color.blue_500));
        } else {
            textView.setTextColor(ContextCompat.getColor(imageView.getContext(), R.color.white));
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPos1 = position;
                textView.setTextColor(ContextCompat.getColor(imageView.getContext(), R.color.blue_500));
            }
        });
        if (selectPos2 == position) {
            like.setChecked(true);
        } else {
            like.setChecked(false);
        }
        like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectPos2 = position;
                if (isChecked) {
                    like.setBackgroundResource(R.drawable.ic_like_fill);
                } else {
                    like.setBackgroundResource(R.drawable.ic_like);
                }
            }
        });
    }
}
