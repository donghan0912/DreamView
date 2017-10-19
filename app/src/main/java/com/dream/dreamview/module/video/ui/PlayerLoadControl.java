package com.dream.dreamview.module.video.ui;

import com.dream.dreamview.util.LogUtil;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.util.Util;

/**
 * Created on 2017/10/12.
 */

public class PlayerLoadControl implements LoadControl {
    /**
     * The default minimum duration of media that the player will attempt to ensure is buffered at all
     * times, in milliseconds.
     */
    public static final int DEFAULT_MIN_BUFFER_MS = 15000;
//    public static final int DEFAULT_MIN_BUFFER_MS = 100;

    /**
     * The default maximum duration of media that the player will attempt to buffer, in milliseconds.
     */
//    public static final int DEFAULT_MAX_BUFFER_MS = 1000;
    public static final int DEFAULT_MAX_BUFFER_MS = 30000;

    /**
     * The default duration of media that must be buffered for playback to start or resume following a
     * user action such as a seek, in milliseconds.
     */
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_MS = 2500;
//    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_MS = 0;

    /**
     * The default duration of media that must be buffered for playback to resume after a rebuffer,
     * in milliseconds. A rebuffer is defined to be caused by buffer depletion rather than a user
     * action.
     */
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS  = 5000;
//    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS  = 0;

    private static final int ABOVE_HIGH_WATERMARK = 0;
    private static final int BETWEEN_WATERMARKS = 1;
    private static final int BELOW_LOW_WATERMARK = 2;

    private final DefaultAllocator allocator;

    private final long minBufferUs;
    private final long maxBufferUs;
    private final long bufferForPlaybackUs;
    private final long bufferForPlaybackAfterRebufferUs;
    private final PriorityTaskManager priorityTaskManager;

    private int targetBufferSize;
    private boolean isBuffering;
    private boolean stopBuffering = false;

    /**
     * Constructs a new instance, using the {@code DEFAULT_*} constants defined in this class.
     */
    public PlayerLoadControl() {
        this(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE));
    }

    public PlayerLoadControl(DefaultAllocator allocator) {
        this(allocator, DEFAULT_MIN_BUFFER_MS, DEFAULT_MAX_BUFFER_MS, DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS);
    }

    public PlayerLoadControl(DefaultAllocator allocator, int minBufferMs, int maxBufferMs,
                              long bufferForPlaybackMs, long bufferForPlaybackAfterRebufferMs) {
        this(allocator, minBufferMs, maxBufferMs, bufferForPlaybackMs, bufferForPlaybackAfterRebufferMs,
                null);
    }

    public PlayerLoadControl(DefaultAllocator allocator, int minBufferMs, int maxBufferMs,
                              long bufferForPlaybackMs, long bufferForPlaybackAfterRebufferMs,
                              PriorityTaskManager priorityTaskManager) {
        this.allocator = allocator;
        minBufferUs = minBufferMs * 1000L;
        maxBufferUs = maxBufferMs * 1000L;
        bufferForPlaybackUs = bufferForPlaybackMs * 1000L;
        bufferForPlaybackAfterRebufferUs = bufferForPlaybackAfterRebufferMs * 1000L;
        this.priorityTaskManager = priorityTaskManager;
    }

    @Override
    public void onPrepared() {
        reset(false);
    }

    @Override
    public void onTracksSelected(Renderer[] renderers, TrackGroupArray trackGroups,
                                 TrackSelectionArray trackSelections) {
        targetBufferSize = 0;
        for (int i = 0; i < renderers.length; i++) {
            if (trackSelections.get(i) != null) {
                targetBufferSize += Util.getDefaultBufferSize(renderers[i].getTrackType());
            }
        }
        allocator.setTargetBufferSize(targetBufferSize);
    }

    @Override
    public void onStopped() {
        reset(true);
    }

    @Override
    public void onReleased() {
        reset(true);
    }

    @Override
    public Allocator getAllocator() {
        return allocator;
    }

    @Override
    public boolean shouldStartPlayback(long bufferedDurationUs, boolean rebuffering) {
        long minBufferDurationUs = rebuffering ? bufferForPlaybackAfterRebufferUs : bufferForPlaybackUs;
        return minBufferDurationUs <= 0 || bufferedDurationUs >= minBufferDurationUs;
    }

    public boolean getStopBuffering() {
        return this.stopBuffering;
    }

    public void setStopBuffering(boolean stopBuffering) {
        this.stopBuffering = stopBuffering;
    }

    //TODO bufferedDurationUs 缓存的时间，当该值大于最大值时，停止缓存 return false
    // TODO 当小于最小值时，继续缓存 return true
    // if you want to continue loading until the end, you can return true
    @Override
    public boolean shouldContinueLoading(long bufferedDurationUs) {
        LogUtil.e("缓存大小" + bufferedDurationUs);
//        LogUtil.e("缓存 " + stopBuffering);
//        if (stopBuffering) {
//            return false;
//        }
        int bufferTimeState = getBufferTimeState(bufferedDurationUs);
        boolean targetBufferSizeReached = allocator.getTotalBytesAllocated() >= targetBufferSize;
        boolean wasBuffering = isBuffering;
        LogUtil.e("??????" + isBuffering);
        isBuffering = bufferTimeState == BELOW_LOW_WATERMARK
                || (bufferTimeState == BETWEEN_WATERMARKS && isBuffering && !targetBufferSizeReached);
        LogUtil.e( isBuffering + "==" + (bufferTimeState == BELOW_LOW_WATERMARK) + "==" + (bufferTimeState == BETWEEN_WATERMARKS) + "==" + !targetBufferSizeReached);
        // 可以调节缓存大小
//        isBuffering = bufferTimeState == BELOW_LOW_WATERMARK
//                || (bufferTimeState == BETWEEN_WATERMARKS && !targetBufferSizeReached);
        if (priorityTaskManager != null && isBuffering != wasBuffering) {
            if (isBuffering) {
                priorityTaskManager.add(C.PRIORITY_PLAYBACK);
            } else {
                priorityTaskManager.remove(C.PRIORITY_PLAYBACK);
            }
        }
//        LogUtil.e( bufferedDurationUs + "==" + isBuffering + "==" + (allocator.getTotalBytesAllocated()) + "==" + targetBufferSize + "==" + targetBufferSizeReached);
        return isBuffering;
//        return true; // 一直缓存，直到结束
    }

    private int getBufferTimeState(long bufferedDurationUs) {
        return bufferedDurationUs > maxBufferUs ? ABOVE_HIGH_WATERMARK
                : (bufferedDurationUs < minBufferUs ? BELOW_LOW_WATERMARK : BETWEEN_WATERMARKS);
    }

    private void reset(boolean resetAllocator) {
        targetBufferSize = 0;
        if (priorityTaskManager != null && isBuffering) {
            priorityTaskManager.remove(C.PRIORITY_PLAYBACK);
        }
        isBuffering = false;
        if (resetAllocator) {
            allocator.reset();
        }
    }
}
