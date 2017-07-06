package com.dream.dreamview;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.module.meinv.BeautyActivity;
import com.dream.dreamview.sample.CustomeToolbarSampleActivity;
import com.dream.dreamview.sample.NavFoldSampleActivity;
import com.dream.dreamview.util.ToastUtil;
import com.dream.dreamview.widget.MultiStatusLayout;

public class MainActivity extends NavBaseActivity implements View.OnClickListener {

    private MultiStatusLayout mLayout;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("首页");
        setDisplayHomeAsUpEnabled(true);
        mLayout = (MultiStatusLayout) findViewById(R.id.status_layout);
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
                startActivity(new Intent(this, NavFoldSampleActivity.class));
//                startActivity(new Intent(this, CustomNavFoldSampleActivity.class));
//                startActivity(new Intent(this, SwipActivity.class));
//                startActivity(new Intent(this, CustomeToolbarSampleActivity.class));
//                startActivity(new Intent(this, RetrofitActivity.class));
//                startActivity(new Intent(this, BeautyActivity.class));
//                startActivity(new Intent(this, MultiTypeActivity.class));

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
}
