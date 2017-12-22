package com.dream.dreamview.module.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.util.ToastUtil;

/**
 * Created on 2017/12/21.
 */

public class SyncWebActivity extends BaseActivity {
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

        webView.loadUrl("file:///android_asset/t2.html");
        webView.addJavascriptInterface(SyncWebActivity.this, "android");


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
}
