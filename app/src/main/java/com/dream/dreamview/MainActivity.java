package com.dream.dreamview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MultiStatusLayout mLayout;

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
}
