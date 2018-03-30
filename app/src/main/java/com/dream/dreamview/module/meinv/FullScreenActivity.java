package com.dream.dreamview.module.meinv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.dream.dreamview.R;

import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by lenovo on 2017/6/25
 */

public class FullScreenActivity extends AppCompatActivity {
    private static final String EXTRA_URL = "extra_url";

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, FullScreenActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.meinv_activity_large);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        String url = getIntent().getStringExtra(EXTRA_URL);
        Glide.with(this).load(url).into(photoView);
    }
}
