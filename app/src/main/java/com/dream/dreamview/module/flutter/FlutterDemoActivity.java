package com.dream.dreamview.module.flutter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.EventChannel;
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
    final FlutterView flutterView = Flutter.createView(this, getLifecycle(), "route1");
    flutterView.addFirstFrameListener(new FlutterView.FirstFrameListener() {
      @Override
      public void onFirstFrame() {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        flutterView.setLayoutParams(params);
        flutterView.removeFirstFrameListener(this);
      }
    });
//    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1, 1);
    addContentView(flutterView,
            params);

  }
}
