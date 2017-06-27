package com.dream.dreamview.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

@SuppressWarnings("unused")
public class ToastUtil {
    @SuppressWarnings("unused")
    private static final int MSG_SHOW = 0;

    @SuppressWarnings("unused")
    private static final Handler sHandler;
    @SuppressWarnings("unused")
    @SuppressLint("StaticFieldLeak")
    private static ToastUtil sToast;

    @SuppressWarnings("unused")
    private final Context mContext;
    @SuppressWarnings("unused")
    private CharSequence mMsg;
    @SuppressWarnings("unused")
    @StringRes
    private int mResId;
    @SuppressWarnings("unused")
    private int mDuration;
    @SuppressWarnings("unused")
    private Toast mToast;
    @SuppressWarnings("unused")
    private View mView;

    static {
        sHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @SuppressWarnings("unused")
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SHOW:
                        sToast.showToast();
                        return true;
                }
                return false;
            }
        });
    }

    @SuppressWarnings("unused")
    private ToastUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    @SuppressWarnings("unused")
    private void setText(CharSequence msg) {
        mMsg = msg;
    }

    @SuppressWarnings("unused")
    private void setText(@StringRes int resId) {
        mResId = resId;
    }

    @SuppressWarnings("unused")
    private void setDuration(int duration) {
        mDuration = duration;
    }

    @SuppressWarnings("unused")
    public static void showShortToast(Context context, CharSequence msg) {
        if (null == sToast) {
            sToast = new ToastUtil(context);
        }
        sToast.setText(msg);
        sToast.setText(0);
        sToast.setDuration(Toast.LENGTH_SHORT);
        sHandler.sendEmptyMessage(MSG_SHOW);
    }

    @SuppressWarnings("unused")
    public static void showShortToast(Context context,@StringRes int resId) {
        if (null == sToast) {
            sToast = new ToastUtil(context);
        }
        sToast.setText(null);
        sToast.setText(resId);
        sToast.setDuration(Toast.LENGTH_SHORT);
        sHandler.sendEmptyMessage(MSG_SHOW);
    }

    @SuppressWarnings("unused")
    public static void showShortToast(Context context, String msg, Object... arg) {
        showShortToast(context, String.format(msg, arg));
    }

    @SuppressWarnings("unused")
    public static void showShortToast(Context context, @StringRes int resId, Object... arg) {
        showShortToast(context, context.getString(resId, arg));
    }

    @SuppressWarnings("unused")
    public static void showLongToast(Context context, CharSequence msg) {
        if (null == sToast) {
            sToast = new ToastUtil(context);
        }
        sToast.setText(msg);
        sToast.setText(0);
        sToast.setDuration(Toast.LENGTH_LONG);
        sHandler.sendEmptyMessage(MSG_SHOW);
    }

    @SuppressWarnings("unused")
    public static void showLongToast(Context context,@StringRes int resId) {
        if (null == sToast) {
            sToast = new ToastUtil(context);
        }
        sToast.setText(null);
        sToast.setText(resId);
        sToast.setDuration(Toast.LENGTH_LONG);
        sHandler.sendEmptyMessage(MSG_SHOW);
    }

    @SuppressWarnings("unused")
    public static void showLongToast(Context context, String msg, Object... arg) {
        showLongToast(context, String.format(msg, arg));
    }

    @SuppressWarnings("unused")
    public static void showLongToast(Context context, @StringRes int resId, Object... arg) {
        showLongToast(context, context.getString(resId, arg));
    }

    @SuppressWarnings("unused")
    private void showToast() {
        getToast();
        if (mResId > 0) {
            mToast.setText(mResId);
        } else {
            mToast.setText(mMsg);
        }
        mToast.setDuration(mDuration);
        mToast.show();
    }

    @SuppressWarnings("unused")
    @SuppressLint("ShowToast")
    private void getToast() {
        if (mToast == null) {
            mToast = new Toast(mContext);
        }
        if (mView == null) {
            mView = Toast.makeText(mContext, "", Toast.LENGTH_SHORT).getView();
        }
        mToast.setView(mView);
    }
}
