package com.dream.dreamview.net;

import java.io.IOException;

/**
 * Created by Administrator on 2017/6/21
 */

public class ResponseException extends IOException {
    public int code;
    public String message;

    public ResponseException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
