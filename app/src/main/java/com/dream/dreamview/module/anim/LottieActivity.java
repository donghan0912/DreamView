package com.dream.dreamview.module.anim;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.dream.dreamview.R;
import com.dream.dreamview.base.BaseActivity;
import com.dream.dreamview.util.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog();
      }
    });
  }

  private void dialog() {
    showProgressDialog("加载中...", false);
    Observable.timer(60000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Long>() {
              @Override
              public void accept(Long aLong) throws Exception {
                dismissProgressDialog();
              }
            });
  }

  private static final String PROGRESS_FRAGMENT_TAG = "progress";

  protected ProgressFragment progressDialog;
  protected boolean fragmentStateSave = true;

  @SuppressWarnings("unused")
  public synchronized void showProgressDialog(String message, boolean outClick) {
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    if (null == progressDialog) {
      progressDialog = (ProgressFragment) manager.findFragmentByTag(PROGRESS_FRAGMENT_TAG);
      if (null == progressDialog) {
        progressDialog = new ProgressFragment();
      }
    }
    Bundle arg = new Bundle();
    arg.putBoolean(ProgressFragment.ARG_CANCELABLE, outClick);
    arg.putString(ProgressFragment.ARG_MSG, message);
    Bundle arguments = progressDialog.getArguments();
    if (null != arguments) {
      arguments.clear();
      arguments.putAll(arg);
    } else {
      progressDialog.setArguments(arg);
    }
    if (fragmentStateSave && !isFinishing() && !progressDialog.isAdded()) {
      progressDialog.show(transaction, PROGRESS_FRAGMENT_TAG);
    }
  }

  /**
   * 关闭progress dialog
   */
  public void dismissProgressDialog() {
    if (progressDialog != null) {
      progressDialog.dismissAllowingStateLoss();
    }
  }

  public static class ProgressFragment extends DialogFragment {

    public static final String ARG_CANCELABLE = "cancelable";
    public static final String ARG_MSG = "msg";

//    private ProgressDialog mDialog;
    private Dialog mDialog;
    private TextView textView;
    private LottieAnimationView animationView;
    private boolean mShow = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      LayoutInflater inflater = getActivity().getLayoutInflater();
      View view = inflater.inflate(R.layout.anim_activity_lottie_dialog, null);
      textView = view.findViewById(R.id.msg);
      animationView = view.findViewById(R.id.animation_view);
      animationView.setImageAssetsFolder("animation/");
      textView.setText(getArguments().getString(ARG_MSG));
      setCancelable(getArguments().getBoolean(ARG_CANCELABLE, true));

      Dialog dialog = new Dialog(getActivity(), R.style.dialogTransparent);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.setContentView(view);
      dialog.setCanceledOnTouchOutside(true);
      Window window = dialog.getWindow();
      window.setBackgroundDrawableResource(R.color.transparent);
      WindowManager.LayoutParams wlp = window.getAttributes();
      wlp.gravity = Gravity.CENTER;
      wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
      wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
      window.setAttributes(wlp);
      return dialog;
    }

    private void setMessage() {
      if (null != mDialog && textView != null) {
        textView.setText(getArguments().getString(ARG_MSG));
      }
    }

    public boolean isShowing() {
      return mShow;
    }

    @Override
    public synchronized int show(FragmentTransaction transaction, String tag) {
      if (!mShow) {
        mShow = true;
        return super.show(transaction, tag);
      } else {
        setMessage();
      }
      return 0;
    }

    @Override
    public synchronized void show(FragmentManager manager, String tag) {
      if (!mShow) {
        mShow = true;
        super.show(manager, tag);
      } else {
        setMessage();
      }
    }

    @Override
    public synchronized void dismiss() {
      if (mShow) {
        mShow = false;
        FragmentManager fm = getFragmentManager();
        if (null != fm) {
          super.dismiss();
        }
        animationView.clearAnimation();
      }
    }

    @Override
    public synchronized void dismissAllowingStateLoss() {
      if (mShow) {
        mShow = false;
        FragmentManager fm = getFragmentManager();
        if (null != fm) {
          super.dismissAllowingStateLoss();
        }
        animationView.clearAnimation();
      }
    }
  }
}
