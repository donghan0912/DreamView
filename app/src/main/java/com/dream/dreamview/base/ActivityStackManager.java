package com.dream.dreamview.base;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Activity 堆栈管理
 */
public class ActivityStackManager {

  private static class SingletonHolder {

    static final ActivityStackManager INSTANCE = new ActivityStackManager();
  }

  //单例模式创建
  public static ActivityStackManager getInstance() {
    return ActivityStackManager.SingletonHolder.INSTANCE;
  }

  private final Stack<WeakReference<Activity>> mActivityStack;

  private ActivityStackManager() {
    mActivityStack = new Stack<>();
  }

  //进栈
  public void addStack(Activity activity) {
    if (activity != null) {
      mActivityStack.add(new WeakReference<>(activity));
    }
  }

  //出栈
  public void removeStack(Activity activity) {
    if (activity != null) {
      for (int i = mActivityStack.size() - 1; i >= 0; i--) {
        WeakReference<Activity> weak = mActivityStack.get(i);
        if (null == weak) {
          mActivityStack.remove(i);
          continue;
        }
        Activity cached = weak.get();
        if (null == cached) {
          mActivityStack.remove(i);
          continue;
        }
        if (cached == activity) {
          mActivityStack.remove(i);
          if (!cached.isFinishing()) {
            cached.finish();
          }
          return;
        }
      }
    }
  }

  //获取当前栈顶
  public Activity getStackTop() {
    for (int i = mActivityStack.size() - 1; i >= 0; i--) {
      WeakReference<Activity> weak = mActivityStack.get(i);
      if (null == weak) {
        mActivityStack.remove(i);
        continue;
      }
      Activity cached = weak.get();
      if (null == cached) {
        mActivityStack.remove(i);
        continue;
      }
      return cached;
    }
    return null;
  }

  public int getStackSize() {
    return mActivityStack.size();
  }

  //获取当前栈顶下面那个Activity
  public Activity getStackTopParent() {
    while (mActivityStack.size() > 1) {
      int pos = mActivityStack.size() - 2;
      WeakReference<Activity> weak = mActivityStack.get(pos);
      if (null == weak || null == weak.get()) {
        mActivityStack.remove(pos);
        continue;
      }
      return weak.get();
    }
    return null;
  }

  public void finishAllActivity() {
    for (int i = 0; i < mActivityStack.size(); i++) {
      WeakReference<Activity> weak = mActivityStack.get(i);
      if (null != weak && null != weak.get()) {
        weak.get().finish();
      }
    }
    mActivityStack.clear();
  }
}
