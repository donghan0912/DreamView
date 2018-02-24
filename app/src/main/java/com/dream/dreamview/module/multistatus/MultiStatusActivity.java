package com.dream.dreamview.module.multistatus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.util.ToastUtil;
import com.dream.dreamview.widget.MultiStatusLayout;

public class MultiStatusActivity extends NavBaseActivity implements View.OnClickListener {

  public static void start(Context context) {
    context.startActivity(new Intent(context, MultiStatusActivity.class));
  }

  private MultiStatusLayout mLayout;

  @Override
  protected int getContentView() {
    return R.layout.multi_status_activity_;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    closeSwipe();
    setTitle("多状态页面");
    setDisplayHomeAsUpEnabled(true);
    mLayout = findViewById(R.id.status_layout);
    findViewById(R.id.btn_1).setOnClickListener(this);
    findViewById(R.id.btn_2).setOnClickListener(this);
    findViewById(R.id.btn_3).setOnClickListener(this);
    findViewById(R.id.btn_4).setOnClickListener(this);
    findViewById(R.id.btn_5).setOnClickListener(this);
    findViewById(R.id.btn_6).setOnClickListener(this);
    findViewById(R.id.btn_7).setOnClickListener(this);
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_1:
        mLayout.showContentView();
        break;
      case R.id.btn_2:
        mLayout.showErrorView();
        break;
      case R.id.btn_3:
        mLayout.showLoadingView();
        break;
      case R.id.btn_4:
        mLayout.showNoNetworkView();
        break;
      case R.id.btn_5:
        mLayout.showLoginView();
        break;
      case R.id.btn_6:
        mLayout.showTimeOutView();
        break;
      case R.id.btn_7:
        mLayout.showEmptyView();
        break;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        ToastUtil.showShortToast(this, "返回");
        break;
      case R.id.setting:
        ToastUtil.showShortToast(this, "设置");
        break;
      case R.id.about:
        ToastUtil.showShortToast(this, "关于");
        break;
      case R.id.contact:
        ToastUtil.showShortToast(this, "联系");
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
