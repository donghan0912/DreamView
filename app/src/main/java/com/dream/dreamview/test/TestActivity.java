package com.dream.dreamview.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dream.dreamview.R;
import com.dream.dreamview.base.NavBaseActivity;

/**
 * Created by lenovo on 2017/7/21
 */

public class TestActivity extends NavBaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.ttt;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
