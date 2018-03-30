package com.dream.dreamview.module.video;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dream.dreamview.R;
import com.dream.dreamview.module.video.ui.ExoPlayerView;

import com.hpu.baserecyclerviewadapter.BaseItem;
import com.hpu.baserecyclerviewadapter.BaseViewHolder;

/**
 * Created on 2017/9/30.
 */

public class VideoItem extends BaseItem<Video> {

    private final Context context;
    private ExoPlayerView exoPlayerView;

    public VideoItem(Video video, int recourseId, Context context) {
        super(video, recourseId);
        this.context = context;
    }

    public ExoPlayerView getExoPlayerView() {
        return exoPlayerView;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int position) {
        exoPlayerView = baseViewHolder.findViewById(R.id.exo_player);
        exoPlayerView.setPlayer(Uri.parse(mData.url));
        final FrameLayout overlayLayout = exoPlayerView.getOverlayFrameLayout();
        View view = View.inflate(context, R.layout.video_activity_video_list_item_over_layout, null);
        overlayLayout.addView(view);
        ImageView overImg = view.findViewById(R.id.over_img);
        Glide.with(context)
                .load(R.drawable.meinv)
//                .circleCrop() //  圆形头像，v4.0新增功能
//                .transform(new RoundedCorners(10)) // 圆角 4.0新增功能
                .into(overImg);
        Button play = view.findViewById(R.id.play_btn);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayerView.play();
            }
        });

//        int firstCompletelyVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
//        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
//        if (firstCompletelyVisibleItemPosition == firstVisibleItemPosition) {
//
//        }
//        Rect localRect = new Rect();
//        boolean localVisibleRect = baseViewHolder.itemView.getLocalVisibleRect(localRect);
    }
}
