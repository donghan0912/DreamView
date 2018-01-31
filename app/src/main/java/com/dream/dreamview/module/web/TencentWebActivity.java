package com.dream.dreamview.module.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dream.dreamview.MyApplication;
import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.util.CommonUtils;
import com.dream.dreamview.util.LogUtil;
import com.dream.dreamview.util.ToastUtil;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

/**
 * Created on 2018/1/9.
 */

public class TencentWebActivity extends BaseActivity implements View.OnTouchListener {
  // TODO 网页生成注意事项：
  // 1. 文字只能一层标签
  // 2. 图片大小，不能超过屏幕！
  private CompositeDisposable mDisposables = new CompositeDisposable();
  private Observable<Object> observable = Observable.empty()
          .subscribeOn(AndroidSchedulers.mainThread())
          .doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
              webView.loadUrl("javascript:selectionChangeListen()");
            }
          });
  private WebView webView;
  private RelativeLayout.LayoutParams imageParames;
  private Button button;

  public static void start(Context context) {
    context.startActivity(new Intent(context, TencentWebActivity.class));
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.web_activity_tencent_web);

    RelativeLayout rootView = findViewById(R.id.root);
    webView = new WebView(MyApplication.getContext());
    webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
    rootView.addView(webView);

    button = findViewById(R.id.btn);
    imageParames = (RelativeLayout.LayoutParams) button.getLayoutParams();
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO 标记
        webView.loadUrl("javascript:getRange()");

      }
    });
    //webView = findViewById(R.id.web_view);
    webView.loadUrl("file:///android_asset/t3.html");
    webView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        webView.loadUrl("javascript:startMark(\"" + startX + "\", \"" + startY + "\")");
        mDisposables.add(observable.subscribe());
        return true;
      }
    });
    webView.setOnTouchListener(this);
    webView.setLongClickable(true);
    webView.addJavascriptInterface(this, "android");
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(false);
    settings.setBuiltInZoomControls(false);
  }

  @JavascriptInterface
  public void move(final String bounds) {
    Observable.empty().doOnComplete(new Action() {
      @Override
      public void run() throws Exception {
        LogUtil.e("当前是否是主线程：" + isInMainThread());
        try {
          JSONObject selectionBoundsObject = new JSONObject(bounds);
          int left = selectionBoundsObject.getInt("left");
          int top = selectionBoundsObject.getInt("top");
          int right = selectionBoundsObject.getInt("right");
          int bottom = selectionBoundsObject.getInt("bottom");
          imageParames.leftMargin = CommonUtils.dp2px(left);
          int height = button.getHeight();
          int y = CommonUtils.dp2px(top) - height;
          int y2 = CommonUtils.dp2px(top) + CommonUtils.getStatusBarHeight() - CommonUtils.getScreenHeight();
          if (CommonUtils.dp2px(bottom) <= 0 || y2 >= 0) {
            button.setVisibility(View.GONE);
          } else {
            button.setVisibility(View.VISIBLE);
            imageParames.topMargin = (y < 0) ? 0 : y;
            button.setLayoutParams(imageParames);
          }
          mDisposables.add(observable.subscribe());
          LogUtil.e(left + "=========" + top + "=========" + right + "=========" + bottom);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
  }

  @JavascriptInterface
  public void remove() {
    Observable.empty().doOnComplete(new Action() {
      @Override
      public void run() throws Exception {
        LogUtil.e("当前是否是主线程：" + isInMainThread());
        mDisposables.clear();
      }
    }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
  }

  @JavascriptInterface
  public void startActivity(final String startId, final String startOffset, final String endId, final String endOffset) {
    Observable.empty().doOnComplete(new Action() {
      @Override
      public void run() throws Exception {
        LogUtil.e("当前是否是主线程：" + isInMainThread());
        mDisposables.clear();
        ToastUtil.showShortToast(TencentWebActivity.this,
                startId + "==" + startOffset + "==" + endId + "==" + endOffset);
        SyncWebActivity.start(TencentWebActivity.this, startId, startOffset, endId, endOffset);
      }
    }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
  }

  private float startX;
  private float startY;

  @Override
  public boolean onTouch(View view, MotionEvent event) {
    startX = CommonUtils.px2dip(event.getRawX());
    startY = CommonUtils.px2dip(event.getRawY()) - CommonUtils.px2dip(CommonUtils.getStatusBarHeight());
    return false;
  }

  @Override
  protected void onDestroy() {
    mDisposables.clear();
    clearWebViewResource();
    super.onDestroy();
  }

  public void clearWebViewResource() {
    if (webView != null) {
      webView.removeAllViews();
      // in android 5.1(sdk:21) we should invoke this to avoid memory leak
      // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
      // android-5.1-webview-memory-leak.html)
      ViewGroup parent = (ViewGroup) webView.getParent();
      if (parent != null) {
        parent.removeView(webView);
      }
      webView.setTag(null);
      webView.clearHistory();
      webView.destroy();
      webView = null;
    }
  }

  public static boolean isInMainThread() {
    return Looper.myLooper() == Looper.getMainLooper();
  }
}
