package com.dream.dreamview.module.anim;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.util.LogUtil;
import com.dream.dreamview.util.ToastUtil;

/**
 * Created on 2018/3/16.
 */

public class LottieActivity extends BaseActivity {

  public static void start(Context context) {
    context.startActivity(new Intent(context, LottieActivity.class));
  }

//  @Override
//  protected int getContentView() {
//    return R.layout.anim_activity_lottie;
//  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.anim_activity_lottie);
    LottieAnimationView animationView = findViewById(R.id.animation_view);
    animationView.setImageAssetsFolder("animation/");
    animationView.addAnimatorListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animator) {
        LogUtil.e("动画开始");
      }

      @Override
      public void onAnimationEnd(Animator animator) {
        LogUtil.e("动画结束");
      }

      @Override
      public void onAnimationCancel(Animator animator) {
        LogUtil.e("动画取消");
      }

      @Override
      public void onAnimationRepeat(Animator animator) {

      }
    });
  }
}
