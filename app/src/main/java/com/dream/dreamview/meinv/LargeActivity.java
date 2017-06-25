package com.dream.dreamview.meinv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by lenovo on 2017/6/25
 */

public class LargeActivity extends NavBaseActivity {
    private static final String EXTRA_URL = "extra_url";

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, LargeActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.meinv_activity_large;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        String url = getIntent().getStringExtra(EXTRA_URL);
        Glide.with(this).load(url).into(photoView);
    }
}
