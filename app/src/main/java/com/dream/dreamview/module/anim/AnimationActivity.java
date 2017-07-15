package com.dream.dreamview.module.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import static android.R.attr.width;
import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.dream.dreamview.R.id.img;
import static com.dream.dreamview.R.id.view;

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
    private TestView bg1;
    private TestView bg2;
    private int i = 5;
    private float j = 0.95f;
    private float z = -5000;
    private float m = -10;

    @Override
    protected int getContentView() {
        return R.layout.anim_activity_animation;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setSlideEnabled(false);
        Button btn = (Button) findViewById(R.id.start);

        textView = (TextView) findViewById(view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sss();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startAnim();
//                start();
//                scale();
//                s();
                i = i -1;
                j = j + 0.01f;
                LogUtil.e("===========" + j);
//                bg1.setTranslationX(i);
//                bg1.setTranslationY(i);
//                bg1.setScaleX(j);
//                bg1.setScaleY(j);
                T();
            }
        });
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);

        layout = (LinearLayout) findViewById(R.id.layout);

        bg1 = (TestView) findViewById(R.id.bg1);
        bg2 = (TestView) findViewById(R.id.bg2);
        bg2.setCallBack(new TestView.CallBack() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void Move(int dx) {
//                int i = -dx + 20;
                LogUtil.e(dx + "======");
//                bg1.setTranslationZ(i);

//                bg1.setRotationY(xxx);

//                bg1.setTranslationX(-dx);
//                bg1.setTranslationY(-dx);
//                bg1.setScaleX(0.9f);
//                bg1.setScaleY(0.9f);

                i = i - 5/600 * dx;
                j = j + (1 - j)/ 18000 * dx;
                z = z + z/1000 * dx;
                m = m - m / 10000 * dx;
                LogUtil.e("===========" + j);
                if (i >= 0) {
//                    bg1.setTranslationX(i);
//                    bg1.setTranslationY(i);
                }
                if (j <= 1.0f) {
                    bg1.setScaleX(j);
                    bg1.setScaleY(j);
                }
                if (z <= 0) {
//                    bg1.setTranslationZ(z);
//                    bg1.setElevation(z);
                }

                if (m <= 0) {
//                    bg1.setRotationY(m);
                }

//                bg1.layout(dx );
            }

        });


        bg1.post(new Runnable() {
            @Override
            public void run() {
                int top = bg1.getTop();
                int left = bg1.getLeft();
                int right = bg1.getRight();
                int bottom = bg1.getBottom();
                LogUtil.e("宽高：" + top + "/" + bottom);
//                bg1.layout(left + 100, top + 100, 100 + right, 100 + bottom);
//                int height = bg1.getHeight();
//                int width = bg1.getWidth();
//                LogUtil.e("宽高：" + height + "/" + width);
//                bg1.layout(0, 0, 100 + width, 100 + height);
            }
        });

        bg1.setScaleX(j);
        bg1.setScaleY(j);
//        bg1.setTranslationX(5);
//        bg1.setTranslationY(5);
//        bg1.setTranslationZ(z);
//        bg1.setElevation(z);
//        bg1.setRotationY(m);
    }
    private int xxx = -10;

    private void T() {
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bg1, "rotationY", -5);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bg1, "translationZ", -55, 0);
        scaleX.setDuration(3000);
        scaleX.start();
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

    private void sss() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bg1, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(bg1, "scaleY", 1f, 1.2f);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(bg1, "translationX", 0, -100);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(bg1, "translationY", 0, -100);
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
