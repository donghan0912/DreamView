package com.dream.dreamview.module.video.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dream.dreamview.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.ResizeMode;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.Assertions;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;


@TargetApi(16)
public final class ExoPlayerView extends FrameLayout {
    private static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    private static final int SCREEN_REVERSE_LANDSCAPE = 11;
    private static final int SCREEN_SENSOR_LANDSCAPE = 12;
    private static final int SCREEN_PORTRAIT = 13;

    private static final int SURFACE_TYPE_NONE = 0;
    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;
    private static final int PROGRESS_BAR_MAX = 1000;

    private AspectRatioFrameLayout contentFrame;
    private View shutterView;
    private View surfaceView;
    private SubtitleView subtitleView;
    private FrameLayout overlayFrameLayout;
    private ImageView playBtn;
    private ImageView pauseBtn;
    private ImageView replayBtn;
    private ProgressBar mLoadingProgressBar;
    private SeekBar mSeekBar;
    private TextView mCenterText;
    private ImageView mCenterIcon;
    private LinearLayout mCenterLayout;
    private ImageView mScreenFull;
    private ImageView mScreenNormal;
    private TextView mEndTime;
    private TextView mCurrentTime;
    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private TextView mSpeed;

    private int controllerShowTimeoutMs;
    private boolean playerAutoRotation;
    private boolean isAttachedToWindow;

    private AudioManager mAudioManager;
    private int mMaxVolume; // 系统亮度最大值
    private float mVolume;// 音量
    private int mScreenWidth;
    private int mSlidingRange;

    private float startX;
    private float startY;
    private long duration;
    private long currentPosition;
    private float mStartVolume;// 初始音量
    private float mCurrentBrightness;
    private float mStartBrightness;
    private boolean adjustingBrightness;
    private boolean adjustingVolume;
    private boolean mSeekBarDraging;

    private boolean isVertical;// 竖直滑动
    private boolean isHorizontal;// 水平滑动
    private boolean mPlayerIsReady;

    private SimpleExoPlayer player;
    private ComponentListener componentListener;
    private Activity mActivity;
    // 屏幕旋转
    private OrientationEventListener mOrientationListener;
    private int oldScreenOrientation;
    private StringBuilder formatBuilder;
    private Formatter formatter;

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

        if (isInEditMode()) {
            ImageView logo = new ImageView(context);
            logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_launcher));
            logo.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            addView(logo);
            return;
        }

        int playerLayoutId = R.layout.exo_player_view;
        int surfaceType = SURFACE_TYPE_TEXTURE_VIEW;
        int resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        controllerShowTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
        boolean controllerAutoShow = false;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.ExoPlayerView, 0, 0);
            try {
                surfaceType = a.getInt(R.styleable.ExoPlayerView_surface_type, surfaceType);
                resizeMode = a.getInt(R.styleable.ExoPlayerView_resize_mode, resizeMode);
                controllerShowTimeoutMs = a.getInt(R.styleable.ExoPlayerView_show_timeout,
                        controllerShowTimeoutMs);
                playerAutoRotation = a.getBoolean(R.styleable.ExoPlayerView_auto_rotation,
                        false);
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

        playBtn = findViewById(R.id.exo_play);
        pauseBtn = findViewById(R.id.exo_pause);
        replayBtn = findViewById(R.id.exo_replay);
        mLoadingProgressBar = findViewById(R.id.loading_progress_bar);
        mScreenFull = findViewById(R.id.screen_full);
        mScreenNormal = findViewById(R.id.screen_normal);
        mSeekBar = findViewById(R.id.seek_bar);
        mEndTime = findViewById(R.id.end_time);
        mCurrentTime = findViewById(R.id.current_time);
        mCenterText = findViewById(R.id.text_center);
        mCenterIcon = findViewById(R.id.icon_center);
        mCenterLayout = findViewById(R.id.layut_center);
        mTopLayout = findViewById(R.id.top_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
        mSpeed = findViewById(R.id.speed);

        playBtn.setOnClickListener(componentListener);
        pauseBtn.setOnClickListener(componentListener);
        replayBtn.setOnClickListener(componentListener);
        mSeekBar.setOnSeekBarChangeListener(componentListener);
        mScreenFull.setOnClickListener(componentListener);
        mScreenNormal.setOnClickListener(componentListener);
        mSpeed.setOnClickListener(componentListener);

        mSeekBar.setMax(PROGRESS_BAR_MAX);
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        shouldShowController(controllerAutoShow);
    }

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

    @SuppressWarnings("ResourceType")
    private void setResizeModeRaw(AspectRatioFrameLayout aspectRatioFrame, int resizeMode) {
        aspectRatioFrame.setResizeMode(resizeMode);
    }

    public void pause() {
        if (player != null) {
            player.setPlayWhenReady(false);
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void play() {
        if (player != null) {
            player.setPlayWhenReady(true);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /**
     * Returns whether the controller is currently visible.
     */
    private boolean isVisible() {
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
        if (mAudioManager != null) {
            mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        if (context instanceof Activity) {
            mActivity = (Activity) context;
            screenOrientationInit();
        }
        screenSizeInit();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
        mVolume = 0;
        mActivity = null;
        mCurrentBrightness = 0;
        if (player != null) {
            player.release();
        }
        mOrientationListener.disable();
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
        if (!mSeekBarDraging) {
            mSeekBar.setProgress(progressBarValue(position));
        }
        mSeekBar.setSecondaryProgress(progressBarValue(bufferedPosition));
        mCurrentTime.setText(getStringForTime(position));
        mEndTime.setText(getStringForTime(duration));
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

    private String getStringForTime(long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500L) / 1000L;
        long seconds = totalSeconds % 60L;
        long minutes = totalSeconds / 60L % 60L;
        long hours = totalSeconds / 3600L;
        formatBuilder.setLength(0);
        return hours > 0L ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString() : formatter.format("%02d:%02d", minutes, seconds).toString();
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
            if (playbackState == Player.STATE_ENDED) {
                replayBtn.setVisibility(VISIBLE);
            } else {
                replayBtn.setVisibility(GONE);
            }
            if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_BUFFERING) {
                mLoadingProgressBar.setVisibility(VISIBLE);
            } else {
                if (playbackState == Player.STATE_ENDED) {
                    // 视频播放完成后 关闭屏幕常亮
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                mPlayerIsReady = true;
                mLoadingProgressBar.setVisibility(GONE);
                if (getControllerVisibility()) {
                    if (player.getPlayWhenReady()) {
                        pauseBtn.setVisibility(VISIBLE);
                    } else {
                        playBtn.setVisibility(VISIBLE);
                    }
                }
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
                    play();
                    shouldShowController(false);
                } else if (view == pauseBtn) {
                    playBtn.setVisibility(VISIBLE);
                    pauseBtn.setVisibility(GONE);
                    pause();
                } else if (view == replayBtn) {
                    replayBtn.setVisibility(GONE);
                    player.seekTo(0);
                    player.setPlayWhenReady(true);
                } else if (view == mScreenFull) {
                    changeOrientation(SCREEN_SENSOR_LANDSCAPE);
                } else if (view == mScreenNormal) {
                    changeOrientation(SCREEN_PORTRAIT);
                } else if (view == mSpeed) {
                    showSpeedPop();
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSeekBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            removeCallbacks(hideAction);// 避免滑动进度条的时候，进度条隐藏
            mSeekBar.setThumb(ContextCompat.getDrawable(getContext(), R.drawable.seek_bar_thumb_pressed));
            mSeekBar.setThumbOffset(0);
            mSeekBarDraging = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mSeekBarDraging = false;
            long positionMs = positionValue(seekBar.getProgress());
            long duration = player == null ? 0 : player.getDuration();
            if (player != null) {
                player.seekTo(positionMs == duration ? positionMs - 200 : positionMs);
            }
            play();
            shouldShowController(false);
            mSeekBar.setThumb(ContextCompat.getDrawable(getContext(), R.drawable.seek_bar_thumb_normal));
            mSeekBar.setThumbOffset(0);
        }
    }

    private void shouldShowController(boolean show) {
        if (show) {
            mTopLayout.setVisibility(VISIBLE);
            mBottomLayout.setVisibility(VISIBLE);
            if (replayBtn.getVisibility() == GONE && mLoadingProgressBar.getVisibility() == GONE) {
                if (player.getPlayWhenReady()) {
                    pauseBtn.setVisibility(VISIBLE);
                } else {
                    playBtn.setVisibility(VISIBLE);
                }
            }
            postDelayed(hideAction, controllerShowTimeoutMs);
            updateProgress();
        } else {
            mTopLayout.setVisibility(GONE);
            mBottomLayout.setVisibility(GONE);
            pauseBtn.setVisibility(GONE);
            playBtn.setVisibility(GONE);
            removeCallbacks(updateProgressAction);
            removeCallbacks(hideAction);
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
                    float value = -dy / mSlidingRange;
                    if ((int) x < mScreenWidth / 2) {
                        if (!adjustingBrightness) {
                            adjustingVolume = true;
                            setVolume(value);
                        }
                    } else {
                        if (!adjustingVolume) {
                            adjustingBrightness = true;
                            setBrightness(value);
                        }
                    }
                    break;
                }
                if (isHorizontal) {
                    setProgress(dx / mSlidingRange);
                    break;
                }
                if (absX > 0 && absX * 0.5f > (absY + 8) && mPlayerIsReady) {// 水平
                    isHorizontal = true;
                    if (getControllerVisibility()) {
                        shouldShowController(false);
                    }
                } else if (absY > 0 && absY * 0.5f > (absX + 8) && mPlayerIsReady) {// 竖直
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
                adjustingBrightness = false;
                adjustingVolume = false;
                break;
        }
        return true;
    }

    private void setBrightness(float value) {
        float brightness = value * 4;
        if (mActivity == null) {
            return;
        }
        WindowManager.LayoutParams windowParams = mActivity.getWindow().getAttributes();
        mCenterIcon.setImageResource(R.drawable.ic_video_brightness);
        if (mStartBrightness == 0) {
            mStartBrightness = 0.5f;
        }
        mCurrentBrightness = mStartBrightness + brightness;
        mCurrentBrightness = windowParams.screenBrightness = Math.min(Math.max(mCurrentBrightness, 0), 1);
        int round = Math.round(windowParams.screenBrightness * 100);
        mActivity.getWindow().setAttributes(windowParams);
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
        mCenterText.setText(getResources().getString(R.string.brightness, round));
        mCenterLayout.setVisibility(VISIBLE);
    }

    private void screenOrientationInit() {
        mOrientationListener = new OrientationEventListener(mActivity, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                try {
                    int flag = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
                    if (flag == 0 && !playerAutoRotation) { // 0 表示手机自动旋转功能未打开 1：表示打开
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
                        changeOrientation(SCREEN_REVERSE_LANDSCAPE);
                        oldScreenOrientation = orientation;
                    }
                } else if (orientation == 270) {
                    if (oldScreenOrientation != orientation) {
                        changeOrientation(SCREEN_SENSOR_LANDSCAPE);
                        oldScreenOrientation = orientation;
                    }
                } else if (orientation == 0) {
                    if (oldScreenOrientation != orientation) {
                        changeOrientation(SCREEN_PORTRAIT);
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

    private void changeOrientation(int screenOrientation) {
        if (screenOrientation == SCREEN_PORTRAIT) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            mScreenFull.setVisibility(VISIBLE);
            mScreenNormal.setVisibility(GONE);
        } else {
            if (screenOrientation == SCREEN_REVERSE_LANDSCAPE) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            } else {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
            mScreenFull.setVisibility(GONE);
            mScreenNormal.setVisibility(VISIBLE);
        }
        screenSizeInit();
    }

    private void screenSizeInit() {
        // when screen orientation changed , should get screen size again
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics screen = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(screen);
        mScreenWidth = screen.widthPixels;
        int heightPixels = screen.heightPixels;
        mSlidingRange = Math.min(heightPixels, mScreenWidth);
    }

    public boolean isLandscape() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            changeOrientation(SCREEN_PORTRAIT);
            return true;
        }
        return false;
    }

    private void showSpeedPop() {
        // 倍速弹窗出现的时候，控制条显示
        removeCallbacks(hideAction);
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(), R.layout.exo_player_speed_pop, null);
//        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(mSpeed);
        final TextView value = view.findViewById(R.id.speed_value);
        value.setText(mSpeed.getText().toString());
        view.findViewById(R.id.speed_less).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                double v = Double.parseDouble(value.getText().toString().substring(0, 4)) - 0.05;
                value.setText(v >= 0.50 ? getResources().getString(R.string.exo_player_speed, v) + " X" : "0.00 X");
            }
        });
        view.findViewById(R.id.speed_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                double v = Double.parseDouble(value.getText().toString().substring(0, 4)) + 0.05;
                value.setText(v <= 2.50 ? getResources().getString(R.string.exo_player_speed, v) + " X" : "2.50 X");
            }
        });
        view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String speedText = value.getText().toString();
                mSpeed.setText(speedText);
                if (player != null) {
                    Float speed = Float.valueOf(speedText.substring(0, 4));
                    player.setPlaybackParameters(new PlaybackParameters(speed, 1));
                }
                popupWindow.dismiss();
                shouldShowController(false);
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shouldShowController(false);
            }
        });
    }
}
