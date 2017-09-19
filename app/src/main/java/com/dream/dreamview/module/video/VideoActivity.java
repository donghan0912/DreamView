package com.dream.dreamview.module.video;

import android.app.KeyguardManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.WindowManager;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.module.video.ui.ExoPlayerView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.exoplayer_view);
        exoPlayerView = findViewById(R.id.exoplayer_view2);
        exoPlayerView.setPlayer(player);
//        simpleExoPlayerView.setPlayer(player);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "yourApplicationName"), bandwidthMeter1);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse("http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4"),
                dataSourceFactory, extractorsFactory, null, null);

        HlsMediaSource hlsMediaSource = new HlsMediaSource(Uri.parse("http://devimages.apple.com/samplecode/adDemo/ad.m3u8"),
                dataSourceFactory, mainHandler, null);

        // Prepare the player with the source.
        player.prepare(videoSource);
//        player.setPlayWhenReady(true);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        t();
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayerView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 待完善
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private OrientationEventListener mOrientationListener;
    private int oldScreenOrientation;
    // TODO 处理屏幕旋转
    private void t() {
        // TODO 打开关闭屏幕常亮功能，ExoPlayerView需要对外暴露一个视频开始结束Listener
        // TODO 配置一个是否常亮开关
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                try {
                    int flag = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
                    if (flag == 0) { // 0 表示手机自动旋转功能未打开 1：表示打开
                        return;
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;  //手机平放时，检测不到有效的角度
                }
                //只检测是否有四个角度的改变
                if (orientation > 350 || orientation < 10) { //0度
                    orientation = 0;
                } else if (orientation > 80 && orientation < 100) { //90度
                    orientation = 90;
                } else if (orientation > 170 && orientation < 190) { //180度
                    orientation = 180;
                } else if (orientation > 260 && orientation < 280) { //270度
                    orientation = 270;
                }
                if (orientation == 90) {
                    if (oldScreenOrientation != orientation) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        oldScreenOrientation = orientation;
                    }
                } else if (orientation == 270) {
                    if (oldScreenOrientation != orientation) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        oldScreenOrientation = orientation;
                    }
                } else if (orientation == 0) {
                    if (oldScreenOrientation != orientation) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                        oldScreenOrientation = orientation;
                    }
                }
            }
        };
        if (mOrientationListener.canDetectOrientation()) {
            mOrientationListener.enable();
        } else {
            mOrientationListener.disable();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationListener.disable();
    }
}
