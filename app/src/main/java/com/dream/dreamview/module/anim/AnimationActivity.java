package com.dream.dreamview.module.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.util.LogUtil;

import static com.dream.dreamview.R.id.img;

/**
 * Created by Administrator on 2017/7/12
 */

public class AnimationActivity extends NavBaseActivity {

    private TextView textView;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private LinearLayout layout;
    private ImageView bg1;
    private ImageView bg2;

    @Override
    protected int getContentView() {
        return R.layout.anim_activity_animation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = (Button) findViewById(R.id.start);
        textView = (TextView) findViewById(R.id.view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startAnim();
//                start();
//                scale();
                s();
            }
        });
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);

        layout = (LinearLayout) findViewById(R.id.layout);

        bg1 = (ImageView) findViewById(R.id.bg1);
        bg2 = (ImageView) findViewById(R.id.bg2);
    }

    private void s() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bg1, "scaleX", 1f, 0.9f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(bg1, "scaleY", 1f, 0.9f);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(bg1, "translationX", 0, 100);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(bg1, "translationY", 0, 100);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, translationX, translationY);
        animatorSet.start();
    }

    private void scale() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(layout, "scaleX", 1f, 0.98f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(layout, "scaleY", 1f, 0.98f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }
    private void start() {

        ObjectAnimator animator11 = ObjectAnimator.ofFloat(img1, "alpha", 1, 0);
        ObjectAnimator animator12 = ObjectAnimator.ofFloat(img2, "alpha", 1, 0);
        ObjectAnimator animator13 = ObjectAnimator.ofFloat(img3, "alpha", 1, 0);
        ObjectAnimator animator14 = ObjectAnimator.ofFloat(img4, "alpha", 1, 0);
        ObjectAnimator animator15 = ObjectAnimator.ofFloat(img5, "alpha", 1, 0);
        animator12.setStartDelay(100);
        animator13.setStartDelay(200);
        animator14.setStartDelay(300);
        animator15.setStartDelay(400);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(img1, "TranslationY", 0, 800);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img2, "TranslationY", 0, 800);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(img3, "TranslationY", 0, 800);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(img4, "TranslationY", 0, 800);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(img5, "TranslationY", 0, 800);
        animator2.setStartDelay(100);
        animator3.setStartDelay(200);
        animator4.setStartDelay(300);
        animator5.setStartDelay(400);
        animator1.setInterpolator(new BounceInterpolator());
        animator2.setInterpolator(new BounceInterpolator());
        animator3.setInterpolator(new BounceInterpolator());
        animator4.setInterpolator(new BounceInterpolator());
        animator5.setInterpolator(new BounceInterpolator());
//        animator.setInterpolator(new MyInterpolator());
//        animator.setEvaluator(new MyEvaluator());
//        animator.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator11, animator12, animator13, animator14, animator15, animator1, animator2, animator3, animator4, animator5);
//        animatorSet.playSequentially(animator1, animator2, animator3, animator4, animator5);
//        animatorSet.play(animator1).before(animator2);
        animatorSet.setDuration(2000);
        animatorSet.start();

    }

    class MyInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float input) {
            float v = input * 2;
            LogUtil.e("input值：" + input + "/" + v);
            return (float)Math.pow(input, 10);
        }
    }

    class MyEvaluator implements TypeEvaluator<Float> {

        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {

            return null;
        }
    }


    private void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 400);
        animator.setEvaluator(new ArgbEvaluator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                LogUtil.e(value + "");
                textView.layout(value, value, value + textView.getWidth(), value + textView.getHeight());
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }
}
