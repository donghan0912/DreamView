package com.dream.dreamview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MultiStatusLayout mLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = (MultiStatusLayout) findViewById(R.id.status_layout);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main_toolbar_menu);
        /*设置更多菜单图标*/
        toolbar.setOverflowIcon(AppCompatResources.getDrawable(this, R.drawable.ic_more));

    }

    private void hide(boolean isShow) {
        if (isShow) {
            toolbar.setNavigationIcon(R.drawable.ic_back);
        } else {
            toolbar.setNavigationIcon(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                mLayout.showContentView();
                hide(false);
                ToolbarActivity.start(this);
                break;
            case R.id.btn_2:
                mLayout.showErrorView();
                hide(true);
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
}
