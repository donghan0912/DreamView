package com.dream.dreamview;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class CommonHandler extends Handler {
    public interface MessageHandler {
        @SuppressWarnings("UnusedParameters")
        void handleMessage(Message msg);
    }

    private final WeakReference<MessageHandler> mMessageHandler;

    public CommonHandler(MessageHandler msgHandler) {
        mMessageHandler = new WeakReference<>(msgHandler);
    }

    @Override
    public void handleMessage(Message msg) {
        MessageHandler realHandler = mMessageHandler.get();
        if (realHandler != null) {
            realHandler.handleMessage(msg);
        }
    }
}
