package com.dream.dreamview.module.video;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.module.video.ui.ExoPlayerView;
import com.dream.dreamview.util.CommonUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;

import static com.dream.dreamview.module.video.ui.ExoPlayerView.SCREEN_PORTRAIT;

/**
 * Created by Administrator on 2017/7/5
 */

public class VideoActivity extends NavBaseActivity {

    private SimpleExoPlayer player;
    private ExoPlayerView exoPlayerView;

    @Override
    protected int getContentView() {
        return R.layout.video_activity_video;
    }

    @Override
    protected int getToolbarLayout() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        closeSwipe();

        exoPlayerView = findViewById(R.id.exoplayer_view);
//        exoPlayerView.setPlayer(player);
        exoPlayerView.setPlayer(Uri.parse("http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4"), true);
//        exoPlayerView.setPlayer(Uri.parse("http://devimages.apple.com/samplecode/adDemo/ad.m3u8"));
        exoPlayerView.setOrientationChangeListener(new ExoPlayerView.OrientationChangeListener() {
            @Override
            public void onOrientationChange(int screenOrientation) {
                if (screenOrientation == SCREEN_PORTRAIT) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) exoPlayerView.getLayoutParams();
                    params.height = exoPlayerHeight;
                    exoPlayerView.setLayoutParams(params);
                } else {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) exoPlayerView.getLayoutParams();
                    params.height = CommonUtils.getScreenHeight();
                    exoPlayerView.setLayoutParams(params);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayerView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return exoPlayerView.isLandscape() || super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private int exoPlayerHeight;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            exoPlayerHeight = exoPlayerView.getHeight();
//            exoPlayerView.play();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onDestroy() {
        exoPlayerView.release();
        super.onDestroy();
    }
}
