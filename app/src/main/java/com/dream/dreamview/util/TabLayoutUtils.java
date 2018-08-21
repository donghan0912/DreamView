package com.dream.dreamview.util;

import android.content.Context;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/6/7.
 */

public class TabLayoutUtils {

  public static void repaintTabs(final Context pContext, final TabLayout tabLayout) {
    tabLayout.post(new Runnable() {
      @Override
      public void run() {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
          ViewGroup tabStripGroup = (ViewGroup) tabStrip;
          int childCount = ((ViewGroup) tabStrip).getChildCount();
          int tabWidth = getScreenWidth(pContext) / childCount;
          for (int i = 0; i < childCount; i++) {
            View tabView = tabStripGroup.getChildAt(i);
            if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
              ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
              int margin = (tabWidth - tabView.getWidth()) / 2;
              params.leftMargin = margin;
              params.rightMargin = margin;
              tabView.setLayoutParams(params);
            }
          }
        }
      }
    });
  }

  private static int getScreenWidth(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.x;
  }
}
