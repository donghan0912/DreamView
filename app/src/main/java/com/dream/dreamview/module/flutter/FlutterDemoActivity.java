package com.dream.dreamview.module.flutter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.dream.dreamview.base.BaseActivity;

import io.flutter.facade.Flutter;
import io.flutter.view.FlutterView;

/**
 * Created on 2018/8/23.
 */
public class FlutterDemoActivity extends BaseActivity {

  private String EVENT_CHANNEL = "sample.flutter.io/get_name";

  public static void start(Context context) {
    Intent intent = new Intent(context, FlutterDemoActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FlutterView flutterView = Flutter.createView(this, getLifecycle(), "route1");
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    addContentView(flutterView,
            params);

  }
}
