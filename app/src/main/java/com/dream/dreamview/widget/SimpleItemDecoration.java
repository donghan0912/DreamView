package cn.wanxue.newgaoshou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * RecyclerView 分割线
 * Created on 2019/4/19.
 */
public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

  private int headHeight;
  private int dividerHeight;
  private int bottomHeight;
  private Paint dividerPaint;
  private final int screenWidth;

  public SimpleItemDecoration(Context context, @ColorRes int id, float top, float divider, float bottom) {
    dividerPaint = new Paint();
    dividerPaint.setColor(context.getResources().getColor(id));
    headHeight = dp2px(context, top);
    dividerHeight = dp2px(context, divider);
    bottomHeight = dp2px(context, bottom);
    screenWidth = context.getResources().getDisplayMetrics().widthPixels;
  }


  /**
   *
   * @param outRect 相当于itemview 下面平铺的一个矩形，outRect.left/top/right/bottom 是边距
   * @param view itemview
   * @param parent recycler对象
   * @param state recycler 滑动状态
   */
  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    int childCount = parent.getChildCount();
    View topView = parent.getChildAt(0);
    View bottomView = parent.getChildAt(childCount - 1);
    // TODO 这里有bug，Recyclerview 的 ItemView 复用，会导致边距错乱
    if (view == topView) {
      outRect.top = headHeight;
      outRect.bottom = dividerHeight;
    } else if (view == bottomView) {
      outRect.bottom = bottomHeight;
    } else {
      outRect.bottom = dividerHeight;
    }
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount - 1; i++) {
      View view = parent.getChildAt(i);
      float top = view.getBottom();
      float bottom = view.getBottom() + dividerHeight;
      if (i == 0) {
        c.drawRect(0, 0, screenWidth, headHeight, dividerPaint);
        c.drawRect(0, top, screenWidth, bottom, dividerPaint);
      } else if (i == childCount - 1) {
        float lastBottom = view.getBottom() + bottomHeight;
        c.drawRect(0, top, screenWidth, lastBottom, dividerPaint);
      } else {
        c.drawRect(0, top, screenWidth, bottom, dividerPaint);
      }
    }
  }

  private int dp2px(Context context, float dpValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
