/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dream.dreamview.module.video.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dream.dreamview.R;
import com.dream.dreamview.util.LogUtil;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.ResizeMode;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.Assertions;

import java.util.List;


@TargetApi(16)
public final class ExoPlayerView extends FrameLayout {
    private static final int SURFACE_TYPE_NONE = 0;
    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;
    private static final int PROGRESS_BAR_MAX = 1000;
    private static final float BRIGHTNESS_DEFAULT_UNIT = 2;
    private static final float VOLUME_DEFAULT_UNIT = 2;

    private final AspectRatioFrameLayout contentFrame;
    private final View shutterView;
    private final View surfaceView;
    private final SubtitleView subtitleView;
    private final ComponentListener componentListener;
    private final FrameLayout overlayFrameLayout;

    private SimpleExoPlayer player;
    private boolean useController;
    private int controllerShowTimeoutMs;
    private boolean controllerAutoShow;
    private boolean controllerHideOnTouch;
    private ImageView playBtn;
    private ImageView pauseBtn;
    private ProgressBar mLoadingProgressBar;
    private SeekBar mSeekBar;
    private TextView mCenterText;
    private ImageView mCenterIcon;
    private LinearLayout mCenterLayout;

    private boolean isAttachedToWindow;
    private boolean isPauseFromUser;// 是否手动暂停

    private ImageView mFullScreen;
    private AudioManager mAudioManager;
    private int mMaxVolume; // 系统亮度最大值
    private float mVolume;// 音量
    private DisplayMetrics mScreen;

    private float startX;
    private float startY;
    private WindowManager.LayoutParams mWindowParams;
    private Activity mActivity;
    private long duration;
    private long currentPosition;
    private float mStartVolume;// 初始音量
    private float mCurrentBrightness;
    private float mStartBrightness;

    private boolean isVertical;// 竖直滑动
    private boolean isHorizontal;// 水平滑动
    private boolean mPlayerIsReady;
    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;

    private final Runnable updateProgressAction = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private final Runnable hideAction = new Runnable() {
        @Override
        public void run() {
            shouldShowController(false);
        }
    };

    public ExoPlayerView(Context context) {
        this(context, null);
    }

    public ExoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int playerLayoutId = R.layout.exo_player_view;
        boolean useController = true;
        int surfaceType = SURFACE_TYPE_TEXTURE_VIEW;
        int resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        int controllerShowTimeoutMs = PlaybackControlView.DEFAULT_SHOW_TIMEOUT_MS;
        boolean controllerHideOnTouch = true;
        boolean controllerAutoShow = true;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.ExoPlayerView, 0, 0);
            try {
                playerLayoutId = a.getResourceId(R.styleable.ExoPlayerView_player_layout_id,
                        playerLayoutId);
                useController = a.getBoolean(R.styleable.ExoPlayerView_use_controller, useController);
                surfaceType = a.getInt(R.styleable.ExoPlayerView_surface_type, surfaceType);
                resizeMode = a.getInt(R.styleable.ExoPlayerView_resize_mode, resizeMode);
                controllerShowTimeoutMs = a.getInt(R.styleable.ExoPlayerView_show_timeout,
                        controllerShowTimeoutMs);
                controllerHideOnTouch = a.getBoolean(R.styleable.ExoPlayerView_hide_on_touch,
                        controllerHideOnTouch);
                controllerAutoShow = a.getBoolean(R.styleable.ExoPlayerView_auto_show,
                        controllerAutoShow);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(playerLayoutId, this);
        componentListener = new ComponentListener();
        // setDescendantFocusability()主要用于控制child View获取焦点的能力
        // 先分发给Child View进行处理，如果所有的Child View都没有处理，则自己再处理
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        // Content frame.
        contentFrame = findViewById(R.id.exo_content_frame);
        if (contentFrame != null) {
            setResizeModeRaw(contentFrame, resizeMode);
        }

        // Shutter view.
        shutterView = findViewById(R.id.exo_shutter);

        // Create a surface view and insert it into the content frame, if there is one.
        if (contentFrame != null && surfaceType != SURFACE_TYPE_NONE) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            surfaceView = surfaceType == SURFACE_TYPE_TEXTURE_VIEW ? new TextureView(context)
                    : new SurfaceView(context);
            surfaceView.setLayoutParams(params);
            contentFrame.addView(surfaceView, 0);
        } else {
            surfaceView = null;
        }

        // Overlay frame layout.
        overlayFrameLayout = findViewById(R.id.exo_overlay);

        // Subtitle view. 字幕
        subtitleView = findViewById(R.id.exo_subtitles);
        if (subtitleView != null) {
            subtitleView.setUserDefaultStyle();
            subtitleView.setUserDefaultTextSize();
        }

        /** 播放菜单 **/
        playBtn = findViewById(R.id.exo_play);
        pauseBtn = findViewById(R.id.exo_pause);
        mLoadingProgressBar = findViewById(R.id.loading_progress_bar);
        mFullScreen = findViewById(R.id.enter_full_screen);
        mSeekBar = findViewById(R.id.seek_bar);
        playBtn.setOnClickListener(componentListener);
        pauseBtn.setOnClickListener(componentListener);
        mSeekBar.setOnSeekBarChangeListener(componentListener);
        mFullScreen.setOnClickListener(componentListener);

        mSeekBar.setMax(PROGRESS_BAR_MAX);

        mCenterText = findViewById(R.id.text_center);
        mCenterIcon = findViewById(R.id.icon_center);
        mCenterLayout = findViewById(R.id.layut_center);
        mTopLayout = findViewById(R.id.top_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
    }

    /**
     * Switches the view targeted by a given {@link SimpleExoPlayer}.
     *
     * @param player        The player whose target view is being switched.
     * @param oldPlayerView The old view to detach from the player.
     * @param newPlayerView The new view to attach to the player.
     */
    public static void switchTargetView(@NonNull SimpleExoPlayer player,
                                        @Nullable ExoPlayerView oldPlayerView, @Nullable ExoPlayerView newPlayerView) {
        if (oldPlayerView == newPlayerView) {
            return;
        }
        // We attach the new view before detaching the old one because this ordering allows the player
        // to swap directly from one surface to another, without transitioning through a state where no
        // surface is attached. This is significantly more efficient and achieves a more seamless
        // transition when using platform provided video decoders.
        if (newPlayerView != null) {
            newPlayerView.setPlayer(player);
        }
        if (oldPlayerView != null) {
            oldPlayerView.setPlayer(null);
        }
    }

    /**
     * Returns the player currently set on this view, or null if no player is set.
     */
    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public void setPlayer(SimpleExoPlayer player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(componentListener);
            this.player.clearTextOutput(componentListener);
            this.player.clearVideoListener(componentListener);
            if (surfaceView instanceof TextureView) {
                this.player.clearVideoTextureView((TextureView) surfaceView);
            } else if (surfaceView instanceof SurfaceView) {
                this.player.clearVideoSurfaceView((SurfaceView) surfaceView);
            }
        }
        this.player = player;
        if (shutterView != null) {
            shutterView.setVisibility(VISIBLE);
        }
        if (player != null) {
            if (surfaceView instanceof TextureView) {
                player.setVideoTextureView((TextureView) surfaceView);
            } else if (surfaceView instanceof SurfaceView) {
                player.setVideoSurfaceView((SurfaceView) surfaceView);
            }
            player.setVideoListener(componentListener);
            player.setTextOutput(componentListener);
            player.addListener(componentListener);
            updateForCurrentTrackSelections();
        } else {
//            hideController();
        }
    }

    /**
     * Sets the resize mode.
     *
     * @param resizeMode The resize mode.
     */
    public void setResizeMode(@ResizeMode int resizeMode) {
        Assertions.checkState(contentFrame != null);
        contentFrame.setResizeMode(resizeMode);
    }

    /**
     * Gets the view onto which video is rendered. This is either a {@link SurfaceView} (default)
     * or a {@link TextureView} if the {@code use_texture_view} view attribute has been set to true.
     *
     * @return Either a {@link SurfaceView} or a {@link TextureView}.
     */
    public View getVideoSurfaceView() {
        return surfaceView;
    }

    /**
     * Gets the overlay {@link FrameLayout}, which can be populated with UI elements to show on top of
     * the player.
     *
     * @return The overlay {@link FrameLayout}, or {@code null} if the layout has been customized and
     * the overlay is not present.
     */
    public FrameLayout getOverlayFrameLayout() {
        return overlayFrameLayout;
    }

    /**
     * Gets the {@link SubtitleView}.
     *
     * @return The {@link SubtitleView}, or {@code null} if the layout has been customized and the
     * subtitle view is not present.
     */
    public SubtitleView getSubtitleView() {
        return subtitleView;
    }


    private void updateForCurrentTrackSelections() {
        if (player == null) {
            return;
        }
        TrackSelectionArray selections = player.getCurrentTrackSelections();
        for (int i = 0; i < selections.length; i++) {
            if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO && selections.get(i) != null) {
                // Video enabled so artwork must be hidden. If the shutter is closed, it will be opened in
                // onRenderedFirstFrame().

                return;
            }
        }
        // Video disabled so the shutter must be closed.
        if (shutterView != null) {
            shutterView.setVisibility(VISIBLE);
        }
    }

    private boolean setArtworkFromMetadata(Metadata metadata) {
        for (int i = 0; i < metadata.length(); i++) {
            Metadata.Entry metadataEntry = metadata.get(i);
            if (metadataEntry instanceof ApicFrame) {
                byte[] bitmapData = ((ApicFrame) metadataEntry).pictureData;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                return setArtworkFromBitmap(bitmap);
            }
        }
        return false;
    }

    private boolean setArtworkFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            if (bitmapWidth > 0 && bitmapHeight > 0) {
                if (contentFrame != null) {
                    contentFrame.setAspectRatio((float) bitmapWidth / bitmapHeight);
                }
//        artworkView.setImageBitmap(bitmap);
//        artworkView.setVisibility(VISIBLE);
                return true;
            }
        }
        return false;
    }


    @SuppressWarnings("ResourceType")
    private void setResizeModeRaw(AspectRatioFrameLayout aspectRatioFrame, int resizeMode) {
        aspectRatioFrame.setResizeMode(resizeMode);
    }

    public void pause() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }


    /**
     * Returns whether the controller is currently visible.
     */
    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        init();
    }


    private void init() {
        Context context = getContext();
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
            mWindowParams = mActivity.getWindow().getAttributes();
            try {
                int brightness = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                mWindowParams.screenBrightness = brightness / 255.0f;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        removeCallbacks(updateProgressAction);
        mVolume = 0;
        mActivity = null;
        mCurrentBrightness = 0;
        mWindowParams = null;
    }

    private int progressBarValue(long position) {
        long duration = player == null ? C.TIME_UNSET : player.getDuration();
        return duration == C.TIME_UNSET || duration == 0 ? 0 : (int) ((position * PROGRESS_BAR_MAX) / duration);
    }

    private long positionValue(int progress) {
        long duration = player == null ? C.TIME_UNSET : player.getDuration();
        return duration == C.TIME_UNSET ? 0 : ((duration * progress) / PROGRESS_BAR_MAX);
    }

    private void updateProgress() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }

        long duration = player == null ? 0 : player.getDuration();
        long position = player == null ? 0 : player.getCurrentPosition();
        long bufferedPosition = player == null ? 0 : player.getBufferedPosition();
        mSeekBar.setProgress(progressBarValue(position));
        mSeekBar.setSecondaryProgress(progressBarValue(bufferedPosition));

        removeCallbacks(updateProgressAction);
        int playbackState = player == null ? Player.STATE_IDLE : player.getPlaybackState();
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            long delayMs;
            if (player.getPlayWhenReady() && playbackState == Player.STATE_READY) {
                delayMs = 1000 - (position % 1000);
                if (delayMs < 200) {
                    delayMs += 1000;
                }
            } else {
                delayMs = 1000;
            }
            postDelayed(updateProgressAction, delayMs);
        }
    }

    private final class ComponentListener implements SimpleExoPlayer.VideoListener,
            TextRenderer.Output, Player.EventListener, OnClickListener, SeekBar.OnSeekBarChangeListener {

        // TextRenderer.Output implementation

        @Override
        public void onCues(List<Cue> cues) {
            if (subtitleView != null) {
                subtitleView.onCues(cues);
            }
        }

        // SimpleExoPlayer.VideoListener implementation
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                       float pixelWidthHeightRatio) {
            if (contentFrame != null) {
                float aspectRatio = height == 0 ? 1 : (width * pixelWidthHeightRatio) / height;
                contentFrame.setAspectRatio(aspectRatio);
            }
        }

        @Override
        public void onRenderedFirstFrame() {
            if (shutterView != null) {
                shutterView.setVisibility(INVISIBLE);
            }
        }

        @Override
        public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {
            updateForCurrentTrackSelections();
        }

        // Player.EventListener implementation

        @Override
        public void onLoadingChanged(boolean isLoading) {
            // Do nothing.
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_BUFFERING) {
                mLoadingProgressBar.setVisibility(VISIBLE);
            } else {
                mPlayerIsReady = true;
                mLoadingProgressBar.setVisibility(GONE);
            }
            updateProgress();
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            // Do nothing.
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            // Do nothing.
        }

        @Override
        public void onPositionDiscontinuity() {
            // Do nothing.
            updateProgress();
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            // Do nothing.
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            // Do nothing.
            updateProgress();
        }

        @Override
        public void onClick(View view) {
            if (player != null) {
                if (view == playBtn) {
                    playBtn.setVisibility(GONE);
                    pauseBtn.setVisibility(VISIBLE);
                    player.setPlayWhenReady(true);
                } else if (view == pauseBtn) {
                    playBtn.setVisibility(VISIBLE);
                    pauseBtn.setVisibility(GONE);
                    player.setPlayWhenReady(false);
                } else if (view == mFullScreen) {
                    // TODO 待整理
                    if (mActivity != null) {
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    }
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSeekBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.seekTo(positionValue(seekBar.getProgress()));
        }
    }

    private void shouldShowController(boolean show) {
        if (show) {
            mTopLayout.setVisibility(VISIBLE);
            mBottomLayout.setVisibility(VISIBLE);
            if (player.getPlayWhenReady()) {
                pauseBtn.setVisibility(VISIBLE);
            } else {
                playBtn.setVisibility(VISIBLE);
            }
//            postDelayed(hideAction, 5000);
        } else {
            mTopLayout.setVisibility(GONE);
            mBottomLayout.setVisibility(GONE);
            pauseBtn.setVisibility(GONE);
            playBtn.setVisibility(GONE);
        }
    }

    private boolean getControllerVisibility() {
        return mTopLayout.getVisibility() == VISIBLE;
    }

    @Override
    public boolean performClick() {
        if (!getControllerVisibility() && !isHorizontal && !isVertical) {
            shouldShowController(true);
        } else {
            shouldShowController(false);
        }
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO 这里最好放在屏幕旋转那里
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mScreen = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mScreen);
        int widthPixels = mScreen.widthPixels;
        int heightPixels = mScreen.heightPixels;
        int slidingRange = Math.min(heightPixels, widthPixels);

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float dx = x - startX;
                float dy = y - startY;
                float absX = Math.abs(dx);
                float absY = Math.abs(dy);
                if (isVertical) {
                    float value = -dy / slidingRange;
                    if ((int) x < widthPixels / 2) {
                        setVolume(value);
                    } else {
                        setBrightness(value);
                    }
                    break;
                }
                if (isHorizontal) {
                    setProgress(dx / slidingRange);
                    break;
                }
                if (absX > 0 && absX * 0.5f > (absY + 8) && !isVertical && mPlayerIsReady) {// 水平
                    isHorizontal = true;
                    if (getControllerVisibility()) {
                        shouldShowController(false);
                    }
                } else if (absY > 0 && absY * 0.5f > (absX + 8) && !isHorizontal && mPlayerIsReady) {// 竖直
                    isVertical = true;
                    if (getControllerVisibility()) {
                        shouldShowController(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isHorizontal && !isVertical) {
                    performClick();
                }
                if (isHorizontal || isVertical) {
                    mCenterLayout.setVisibility(GONE);
                }
                // 当滑动到100%不显示当前视频页面，是个bug，所以最后减少一点
                if (isHorizontal) {
                    player.seekTo(currentPosition == duration ? currentPosition - 200 : currentPosition);
                }
                isHorizontal = false;
                isVertical = false;
                mStartVolume = 0;
                mStartBrightness = 0;
                break;
        }
        return true;
    }

    private void setBrightness(float value) {
        float brightness = value * 4;
        if (mActivity == null) {
            return;
        }
        mCenterIcon.setImageResource(R.drawable.ic_video_brightness);
        if (mStartBrightness == 0) {
            mStartBrightness = mWindowParams.screenBrightness;
        }
        mCurrentBrightness = mStartBrightness + brightness;
        mCurrentBrightness = mWindowParams.screenBrightness = Math.min(Math.max(mCurrentBrightness, 0), 1);
        int round = Math.round(mWindowParams.screenBrightness * 100);
        mActivity.getWindow().setAttributes(mWindowParams);
        mCenterText.setText(getResources().getString(R.string.brightness, round));
        mCenterLayout.setVisibility(VISIBLE);
    }

    private void setVolume(float value) {
        mCenterIcon.setImageResource(R.drawable.ic_video_volume);
        if (mStartVolume == 0) {
            // 获取当前音量
            mStartVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        float v = value * mMaxVolume * 4;
        mVolume = mStartVolume + v;
        mVolume = Math.max(Math.min(mVolume, mMaxVolume), 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) mVolume, 0);
        int round = (int) (mVolume * 100 / mMaxVolume);
        mCenterText.setText(getResources().getString(R.string.brightness, round));
        mCenterLayout.setVisibility(VISIBLE);
    }

    private void setProgress(float value) {
        mCenterIcon.setImageResource(R.drawable.ic_video_progress);
        if (duration == 0) {
            duration = player.getDuration();
        }
        long position = player.getCurrentPosition();
        currentPosition = position + (long) (value * duration);
        currentPosition = Math.max(Math.min(currentPosition, duration), 0);
        int round = (int) (currentPosition * 100 / ((float) duration));
        LogUtil.e("当前百分比" + round + "========" + currentPosition);
        mCenterText.setText(getResources().getString(R.string.brightness, round));
        mCenterLayout.setVisibility(VISIBLE);
    }
}
