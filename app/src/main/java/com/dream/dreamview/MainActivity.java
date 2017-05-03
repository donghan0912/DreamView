package com.dream.dreamview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dream.dreamview.base.NavBaseActivity;
import com.dream.dreamview.sample.CustomNavFoldSampleActivity;
import com.dream.dreamview.test.TestActivity;
import com.dream.dreamview.widget.MultiStatusLayout;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        T t = new T("hello jackson");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            String json = mapper.writeValueAsString(mapper);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                mLayout.showContentView();
//                startActivity(new Intent(this, NavFoldSampleActivity.class));
//                startActivity(new Intent(this, CustomNavFoldSampleActivity.class));
//                startActivity(new Intent(this, SwipActivity.class));
                startActivity(new Intent(this, TestActivity.class));

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
