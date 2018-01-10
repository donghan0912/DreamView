package com.dream.dreamview.module.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;


import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.util.CommonUtils;
import com.dream.dreamview.util.LogUtil;
import com.dream.dreamview.util.ToastUtil;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * Created on 2017/12/21.
 */

public class SyncWebActivity extends BaseActivity implements View.OnTouchListener {
    private static final String RANGE_START_ID = "range_start_id";
    private static final String RANGE_END_ID = "range_end_id";
    private static final String RANGE_START_OFFSET = "range_start_offset";
    private static final String RANGE_END_OFFSET = "range_end_offset";
    private WebView webView;
    private String startId;
    private String startOffset;
    private String endId;
    private String endOffset;


    public static void start(Context context, String startId, String startOffset, String endId, String endOffset) {
        Intent intent = new Intent(context, SyncWebActivity.class);
        intent.putExtra(RANGE_START_ID, startId);
        intent.putExtra(RANGE_START_OFFSET, startOffset);
        intent.putExtra(RANGE_END_ID, endId);
        intent.putExtra(RANGE_END_OFFSET, endOffset);
        context.startActivity(intent);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity_sync_web);
        webView = findViewById(R.id.web_view);
        webView.setOnTouchListener(this);
        webView.loadUrl("file:///android_asset/t3.html");
        webView.addJavascriptInterface(SyncWebActivity.this, "android");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);

        Intent intent = getIntent();
        startId = intent.getStringExtra(RANGE_START_ID);
        startOffset = intent.getStringExtra(RANGE_START_OFFSET);
        endId = intent.getStringExtra(RANGE_END_ID);
        endOffset = intent.getStringExtra(RANGE_END_OFFSET);

//        webView.loadUrl("javascript:init(startId, startOffset, endId, endOffset)");


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("javascript:init(\"" + startId + "\", \"" + startOffset + "\", \"" + endId + "\", \"" + endOffset + "\")");
            }
        });
    }

    @JavascriptInterface
    public void testInit(final String startId, final String startOffset, final String endId, final String endOffset){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShortToast(SyncWebActivity.this,
                        startId + "==" + startOffset + "==" + endId + "==" + endOffset) ;
//                ToastUtil.showShortToast(SyncWebActivity.this, "被调用了") ;
            }
        });
    }

    @JavascriptInterface
    public void showDeleteMenu(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShortToast(SyncWebActivity.this, "删除按钮") ;
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float xPoint = CommonUtils.px2dip(event.getRawX());
        float yPoint = CommonUtils.px2dip(event.getRawY()) - CommonUtils.px2dip(CommonUtils.getStatusBarHeight());
        LogUtil.e("当前坐标：" + xPoint + "/" + yPoint);
        webView.loadUrl("javascript:isIncluded(\"" + xPoint + "\", \"" + yPoint + "\", \"" + startId + "\", \"" + startOffset + "\", \"" + endId + "\", \"" + endOffset + "\")");
        return false;
    }
}
