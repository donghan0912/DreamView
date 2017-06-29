package com.dream.dreamview.module.meinv;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.dreamview.R;
import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;


/**
 * Created by lenovo on 2017/6/21
 */

public class ImgItem extends BaseItem<Gallery> {
    private int selectPos1 = -1;

//    public ImgItem(Gallery gallery) {
//        super(gallery);
//    }

    private Context context;
    private Gallery gallery;
    public ImgItem(Context context, Gallery gallery) {
//        super(gallery);
        this.context = context;
        this.gallery = gallery;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.item_activity_beauty;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        holder.setText(R.id.desc, gallery.desc);
        final ImageView imageView = holder.findViewById(R.id.img);
        final TextView textView = holder.findViewById(R.id.desc);
        final CheckBox like = holder.findViewById(R.id.like);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = gallery.height;
        imageView.setLayoutParams(params);
        Glide.with(context).load(gallery.url).into(imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenActivity.start(context, gallery.largeUrl);
            }
        });
        if (selectPos1 == position) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPos1 = position;
                textView.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
            }
        });

        like.setChecked(gallery.isChecked);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.setChecked(gallery.isChecked = like.isChecked());
            }
        });
    }
}
